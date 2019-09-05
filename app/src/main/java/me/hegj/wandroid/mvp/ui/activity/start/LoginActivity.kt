package me.hegj.wandroid.mvp.ui.activity.start

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import butterknife.OnClick
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.LoginFreshEvent
import me.hegj.wandroid.app.utils.CacheUtil
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.di.component.start.DaggerLoginComponent
import me.hegj.wandroid.di.module.start.LoginModule
import me.hegj.wandroid.mvp.contract.start.LoginContract
import me.hegj.wandroid.mvp.model.entity.UserInfoResponse
import me.hegj.wandroid.mvp.presenter.start.LoginPresenter
import me.hegj.wandroid.mvp.ui.BaseActivity

/**
 * 登录
 */
class LoginActivity : BaseActivity<LoginPresenter>(), LoginContract.View {

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerLoginComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .loginModule(LoginModule(this))
                .build()
                .inject(this)
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_login //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    /**
     * 初始化界面操作
     */
    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "登录"
            setNavigationIcon(R.drawable.ic_close)
            setNavigationOnClickListener { finish() }
        }
        SettingUtil.setShapColor(login_sub, SettingUtil.getColor(this))
        login_goregister?.setTextColor(SettingUtil.getColor(this))
        login_username.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    login_clear.visibility = View.VISIBLE
                } else {
                    login_clear.visibility = View.GONE
                }
            }
        })
        login_pwd.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    login_key.visibility = View.VISIBLE
                } else {
                    login_key.visibility = View.GONE
                }
            }
        })
        login_key.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                login_pwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                login_pwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            login_pwd.setSelection(login_pwd.text.toString().length)
        }
    }

    @OnClick(R.id.login_clear, R.id.login_sub, R.id.login_goregister)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.login_clear -> login_username.setText("")
            R.id.login_sub -> {
                when {
                    login_username.text.isEmpty() -> showMessage("请填写账号")
                    login_pwd.text.isEmpty() -> showMessage("请填写密码")
                    else -> mPresenter?.login(login_username.text.toString(), login_pwd.text.toString())
                }
            }
            R.id.login_goregister -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }

        }
    }

    override fun onSucc(userinfo: UserInfoResponse) {
        CacheUtil.setUser(userinfo)//保存账户信息
        //保存账户与密码，在其他接口请求的时候当做Cookie传到Header中
        LoginFreshEvent(true, userinfo.collectIds).post()//通知其他界面登录成功了，有收藏的地方需要刷新一下数据
        finish()
    }

}
