package me.hegj.wandroid.mvp.ui.activity.main.home.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.CollectEvent
import me.hegj.wandroid.app.event.LoginFreshEvent
import me.hegj.wandroid.app.utils.RecyclerViewUtils
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.CollectView
import me.hegj.wandroid.app.weight.DefineLoadMoreView
import me.hegj.wandroid.app.weight.loadCallBack.EmptyCallback
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.di.component.main.home.search.DaggerSearchResultComponent
import me.hegj.wandroid.di.module.main.home.search.SearchResultModule
import me.hegj.wandroid.mvp.contract.main.home.search.SearchResultContract
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.AriticleResponse
import me.hegj.wandroid.mvp.presenter.main.home.search.SearchResultPresenter
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.AriticleAdapter
import org.greenrobot.eventbus.Subscribe


/** 搜索结果
 * @Author:         hegaojian
 * @CreateDate:     2019/8/19 9:37
 */

class SearchResultActivity : BaseActivity<SearchResultPresenter>(), SearchResultContract.View {
    private var initPageNo = 0 //注意，页码是从0开始的！！！！！
    var pageNo = initPageNo
    lateinit var loadsir: LoadService<Any>
    lateinit var searchKey: String//搜索关键词
    lateinit var ariticleAdapter: AriticleAdapter//适配器
    private var footView: DefineLoadMoreView? = null
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerSearchResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchResultModule(SearchResultModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_searchresult
    }

    override fun initData(savedInstanceState: Bundle?) {
        searchKey = intent.getStringExtra("searchKey")
        toolbar.run {
            setSupportActionBar(this)
            title = searchKey
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(swipeRefreshLayout) {
            //界面加载失败，或者没有数据时，点击重试的监听
            loadsir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getAriList(pageNo, searchKey)
        }.apply {
            SettingUtil.setLoadingColor(this@SearchResultActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter 并设置监听
        ariticleAdapter = AriticleAdapter(arrayListOf(), true).apply {
            if (SettingUtil.getListMode(this@SearchResultActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@SearchResultActivity))
            } else {
                closeLoadAnimation()
            }
            setOnCollectViewClickListener(object : AriticleAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    //点击爱心收藏执行操作
                    if (v.isChecked) {
                        mPresenter?.uncollect(data[position].id, position)
                    } else {
                        mPresenter?.collect(data[position].id, position)
                    }
                }
            })

            setOnItemClickListener { _, view, position ->
                //点击了整行
                val intent = Intent(this@SearchResultActivity, WebviewActivity::class.java)
                val bundle = Bundle().also {
                    it.putSerializable("data", data[position])
                    it.putString("tag", this@SearchResultActivity::class.java.simpleName)
                    it.putInt("position", position)
                }
                intent.putExtras(bundle)
                launchActivity(intent)
            }
        }
        floatbtn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(this@SearchResultActivity)
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
            setColorSchemeColors(SettingUtil.getColor(this@SearchResultActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getAriList(pageNo, searchKey)
            }
        }
        //初始化recyclerview
        footView = RecyclerViewUtils().initRecyclerView(this, swiperecyclerview, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getAriList(pageNo, searchKey)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(this@SearchResultActivity))
        }

        //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
        swiperecyclerview.run {
            adapter = ariticleAdapter
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
        mPresenter?.getAriList(pageNo, searchKey)//发起请求
    }

    /**
     * 获取文章数据成功
     */
    @SuppressLint("RestrictedApi")
    override fun requestAritilSucces(ariticles: ApiPagerResponse<MutableList<AriticleResponse>>) {
        swipeRefreshLayout.isRefreshing = false
        if (pageNo == initPageNo && ariticles.datas.size == 0) {
            //如果是第一页，并且没有数据，页面提示空布局
            loadsir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadsir.showSuccess()
            //如果是刷新的话，floatbutton就要隐藏了，因为这时候肯定是要在顶部的
            floatbtn.visibility = View.INVISIBLE
            ariticleAdapter.setNewData(ariticles.datas)
        } else {
            //不是第一页
            loadsir.showSuccess()
            ariticleAdapter.addData(ariticles.datas)
        }
        pageNo++
        if (ariticles.pageCount >= pageNo) {
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

    /**
     * 获取文章数据失败
     */
    override fun requestAritilFaild(errorMsg: String) {
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

    /**
     * 收藏文章回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, ariticleAdapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了， 当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in ariticleAdapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了，把所有的收藏全部变为未收藏
            for (item in ariticleAdapter.data) {
                item.collect = false
            }
        }
        ariticleAdapter.notifyDataSetChanged()
    }

    /**
     * 在详情中收藏时，接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var indexResult = -1
                for (index in ariticleAdapter.data.indices) {
                    if (ariticleAdapter.data[index].id == event.id) {
                        ariticleAdapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run {
                if (await() != -1) {
                    ariticleAdapter.notifyItemChanged(await())
                }
            }
        }

    }

}
