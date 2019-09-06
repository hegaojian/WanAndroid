package me.hegj.wandroid.mvp.ui.activity.todo

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import butterknife.OnClick
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.AddTodoEvent
import me.hegj.wandroid.app.utils.DatetimeUtil
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.PriorityDialog
import me.hegj.wandroid.app.weight.PriorityDialog.PriorityInterface
import me.hegj.wandroid.di.component.todo.DaggerAddTodoComponent
import me.hegj.wandroid.di.module.todo.AddTodoModule
import me.hegj.wandroid.mvp.contract.todo.AddTodoContract
import me.hegj.wandroid.mvp.model.entity.TodoResponse
import me.hegj.wandroid.mvp.model.entity.enums.TodoType
import me.hegj.wandroid.mvp.presenter.todo.AddTodoPresenter
import me.hegj.wandroid.mvp.ui.BaseActivity
import java.util.*

/**  添加待办清单
 * @Author:         hegaojian
 * @CreateDate:     2019/9/3 21:45
 */
class AddTodoActivity : BaseActivity<AddTodoPresenter>(), AddTodoContract.View {
    var todoResponse: TodoResponse? = null
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerAddTodoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addTodoModule(AddTodoModule(this))
                .build()
                .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_add_todo //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        intent.run {
            todoResponse = getSerializableExtra("data") as TodoResponse?
        }
        toolbar.run {
            setSupportActionBar(this)
            title = "添加待办清单"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        if (todoResponse == null) {
            add_todo_colorview.setView(TodoType.TodoType1.color)
            add_todo_prox.text = TodoType.TodoType1.content
        } else {
            toolbar.title = "编辑待办清单"
            todoResponse?.let {
                add_todo_title.setText(it.title)
                add_todo_content.setText(it.content)
                add_todo_date.text = it.dateStr
                add_todo_colorview.setView(TodoType.byType(it.priority).color)
                add_todo_prox.text = TodoType.byType(it.priority).content
            }
        }
        SettingUtil.setShapColor(add_todo_submit, SettingUtil.getColor(this))
    }

    @OnClick(R.id.add_todo_date, R.id.add_todo_proxlinear, R.id.add_todo_submit)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.add_todo_date -> {
                MaterialDialog(this).show {
                    cornerRadius(0f)
                    datePicker(minDate = Calendar.getInstance()) { dialog, date ->
                        this@AddTodoActivity.add_todo_date.text = DatetimeUtil.formatDate(date.time, DatetimeUtil.DATE_PATTERN)
                    }
                }
            }
            R.id.add_todo_proxlinear -> {
                PriorityDialog(this, TodoType.byValue(this@AddTodoActivity.add_todo_prox.text.toString()).type).apply {
                    setPriorityInterface(object : PriorityInterface {
                        override fun onSelect(type: TodoType) {
                            this@AddTodoActivity.add_todo_prox.text = type.content
                            this@AddTodoActivity.add_todo_colorview.setView(type.color)
                        }
                    })
                }.show()
            }
            R.id.add_todo_submit -> {
                if (TextUtils.isEmpty(add_todo_title.text.toString())) {
                    showMessage("请填写标题")
                } else if (TextUtils.isEmpty(add_todo_content.text.toString())) {
                    showMessage("请填写内容")
                } else if (TextUtils.isEmpty(add_todo_date.text.toString())) {
                    showMessage("请选择预计完成时间")
                } else {
                    if(todoResponse==null){
                        MaterialDialog(this).show {
                            title(R.string.title)
                            message(text = "确定要添加吗？")
                            positiveButton(text = "添加") {
                                mPresenter?.addTodo(this@AddTodoActivity.add_todo_title.text.toString(),
                                        this@AddTodoActivity.add_todo_content.text.toString(),
                                        this@AddTodoActivity.add_todo_date.text.toString(),
                                        TodoType.byValue(this@AddTodoActivity.add_todo_prox.text.toString()).type
                                )
                            }
                            negativeButton(R.string.cancel)
                        }
                    }else{
                        MaterialDialog(this).show {
                            title(R.string.title)
                            message(text = "确定要提交编辑吗？")
                            positiveButton(text = "提交") {
                                mPresenter?.updateTodo(this@AddTodoActivity.add_todo_title.text.toString(),
                                        this@AddTodoActivity.add_todo_content.text.toString(),
                                        this@AddTodoActivity.add_todo_date.text.toString(),
                                        TodoType.byValue(this@AddTodoActivity.add_todo_prox.text.toString()).type,
                                        todoResponse!!.id
                                )
                            }
                            negativeButton(R.string.cancel)
                        }
                    }

                }
            }
        }
    }

    override fun addTodoSucc() {
        AddTodoEvent().post()
        finish()
    }

    override fun addTodoFaild(errorMsg: String) {
        showMessage(errorMsg)
    }
}
