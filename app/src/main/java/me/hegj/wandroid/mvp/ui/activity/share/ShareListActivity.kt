package me.hegj.wandroid.mvp.ui.activity.share

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems

import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.activity_integral.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

import me.hegj.wandroid.di.component.share.DaggerShareListComponent
import me.hegj.wandroid.di.module.share.ShareListModule
import me.hegj.wandroid.mvp.contract.share.ShareListContract
import me.hegj.wandroid.mvp.presenter.share.ShareListPresenter

import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.AddEvent
import me.hegj.wandroid.app.event.AddEvent.Companion.DELETE_CODE
import me.hegj.wandroid.app.event.CollectEvent
import me.hegj.wandroid.app.utils.RecyclerViewUtils
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.DefineLoadMoreView
import me.hegj.wandroid.app.weight.loadCallBack.EmptyCallback
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.mvp.model.entity.ShareResponse
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.activity.todo.AddTodoActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.ShareAdapter
import org.greenrobot.eventbus.Subscribe

/**
 *  我分享的文章列表
  * @Author:         hegaojian
  * @CreateDate:     2019/10/8 13:28
 */
class ShareListActivity : BaseActivity<ShareListPresenter>(), ShareListContract.View {

    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: ShareAdapter

    private var initPageNo = 1
    private var pageNo: Int = initPageNo //当前页码
    private var footView: DefineLoadMoreView? = null

    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerShareListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .shareListModule(ShareListModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_share_list //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }


    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "我分享的文章"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(swipeRefreshLayout) {
            //界面加载失败，或者没有数据时，点击重试的监听
            loadsir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getShareData(pageNo)
        }.apply {
            SettingUtil.setLoadingColor(this@ShareListActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter
        adapter = ShareAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(this@ShareListActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@ShareListActivity))
            } else {
                closeLoadAnimation()
            }
            setOnItemClickListener { adapter, view, position ->
                launchActivity(Intent(this@ShareListActivity, WebviewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", this@ShareListActivity.adapter.data[position])
                        putString("tag", this@ShareListActivity::class.java.simpleName)
                        putInt("position", position)
                    })
                })
            }
            setOnItemChildClickListener { adapter, view, position ->
                when(view.id){
                    R.id.item_share_del ->{
                        MaterialDialog(this@ShareListActivity).show {
                            title(R.string.title)
                            message(text = "确认删除该文章吗？")
                            positiveButton(text = "删除"){
                                mPresenter?.delAriticle(this@ShareListActivity.adapter.data[position].id,position)
                            }
                            negativeButton(text = "取消")
                        }
                    }
                }
            }
        }
        floatbtn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(this@ShareListActivity)
            setOnClickListener {
                val layoutManager = swiperecyclerview.layoutManager as LinearLayoutManager
                //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    swiperecyclerview.scrollToPosition(0)//没有动画迅速返回到顶部(马上)
                } else {
                    swiperecyclerview.smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
                }
            }
        }
        //初始化 swipeRefreshLayout
        swipeRefreshLayout.run {
            setColorSchemeColors(SettingUtil.getColor(this@ShareListActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getShareData(pageNo)
            }
        }
        //初始化recyclerview
        footView = RecyclerViewUtils().initRecyclerView(this, swiperecyclerview, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getShareData(pageNo)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(this@ShareListActivity))
        }

        //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
        swiperecyclerview.run {
            adapter = this@ShareListActivity.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("RestrictedApi")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(-1)) {
                        floatbtn.visibility = View.INVISIBLE
                    }
                }
            })
        }
        //发起请求
        mPresenter?.getShareData(pageNo)

    }

    override fun requestDataSucces(shareResponse: ShareResponse) {
         swipeRefreshLayout.isRefreshing = false
        if (pageNo == initPageNo && shareResponse.shareArticles.datas.size == 0) {
            //如果是第一页，并且没有数据，页面提示空布局
            loadsir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadsir.showSuccess()
            //如果是刷新的话，floatbutton就要隐藏了，因为这时候肯定是要在顶部的
            floatbtn.visibility = View.INVISIBLE
            adapter.setNewData(shareResponse.shareArticles.datas)
        } else {
            //不是第一页
            loadsir.showSuccess()
            adapter.addData(shareResponse.shareArticles.datas)
        }
        pageNo++
        if (shareResponse.shareArticles.pageCount >= pageNo) {
            //如果总条数大于当前页数时 还有更多数据
            swiperecyclerview.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要时间的，还没刷新完，这里就已经执行了没有更多数据
                //所以在界面上会出现一个小bug，刷新最后一页的时候，没有更多数据啦提示先展示出来了，然后才会加载出请求到的数据
                //暂时还没有找到好的方法，就用这个处理一下，如果觉得没什么影响的可以去掉这个延时操作，或者有更好的解决方式可以告诉我一下
                swiperecyclerview.loadMoreFinish(false, false)
            }, 200)
        }
    }

    override fun requestDataFaild(errorMsg: String) {
        swipeRefreshLayout.isRefreshing = false
        if (pageNo == initPageNo) {
            //如果页码是 初始页 说明是刷新，界面切换成错误页
            loadsir.setCallBack(ErrorCallback::class.java) { _, view ->
                //设置错误页文字错误提示
                view.findViewById<TextView>(R.id.error_text).text = errorMsg
            }
            //设置错误
            loadsir.showCallback(ErrorCallback::class.java)
        } else {
            //页码不是0 说明是加载更多时出现的错误，设置recyclerview加载错误，
            swiperecyclerview.loadMoreError(0, errorMsg)
        }
    }

    override fun deleteShareDataSucc(position: Int) {
        //删除 文章 成功
        adapter.remove(position)
        AddEvent(DELETE_CODE).post()
        if (adapter.data.size == 0) {
            pageNo = initPageNo
            mPresenter?.getShareData(pageNo)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.todo_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.todo_add -> {
                launchActivity(Intent(this, ShareAriticleActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 接收到添加了分享文章的通知
     */
    @Subscribe
    fun addEvent(event: AddEvent){
        if(event.code== AddEvent.SHARE_CODE){
            //刷新
            swipeRefreshLayout.isRefreshing = true
            pageNo = initPageNo
            mPresenter?.getShareData(pageNo)
        }
    }

    /**
     * 在详情中收藏时，接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        for (index in adapter.data.indices) {
            if (adapter.data[index].id == event.id) {
                adapter.data[index].collect = event.collect
                break
            }
        }
    }

}
