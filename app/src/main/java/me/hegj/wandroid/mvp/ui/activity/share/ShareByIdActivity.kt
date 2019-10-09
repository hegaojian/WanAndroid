package me.hegj.wandroid.mvp.ui.activity.share

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseViewHolder
import com.jess.arms.di.component.AppComponent
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.activity_share_by_id.*
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
import me.hegj.wandroid.di.component.share.DaggerShareByIdComponent
import me.hegj.wandroid.di.module.share.ShareByIdModule
import me.hegj.wandroid.mvp.contract.share.ShareByIdContract
import me.hegj.wandroid.mvp.model.entity.ShareResponse
import me.hegj.wandroid.mvp.presenter.share.ShareByIdPresenter
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.AriticleAdapter
import org.greenrobot.eventbus.Subscribe

class ShareByIdActivity : BaseActivity<ShareByIdPresenter>(), ShareByIdContract.View {
    var id:Int = 0
    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: AriticleAdapter
    private var initPageNo = 1
    private var pageNo: Int = initPageNo //当前页码
    private var footView: DefineLoadMoreView? = null
    override fun setupActivityComponent(appComponent: AppComponent) {
        DaggerShareByIdComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .shareByIdModule(ShareByIdModule(this))
                .build()
                .inject(this)
    }


    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_share_by_id //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "他的信息"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        share_layout.setBackgroundColor(SettingUtil.getColor(this))
        ArmsUtils.obtainAppComponentFromContext(this).imageLoader().loadImage(this.applicationContext,
                ImageConfigImpl
                        .builder()
                        .url("https://avatars2.githubusercontent.com/u/18655288?s=460&v=4")
                        .imageView(share_logo)
                        .errorPic(R.drawable.ic_account)
                        .fallback(R.drawable.ic_account)
                        .placeholder(R.drawable.ic_account)
                        .isCrossFade(true)
                        .isCircle(true)
                        .build())
        id = intent.getIntExtra("id",0)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(share_linear) {
            //界面加载失败，或者没有数据时，点击重试的监听
            loadsir.showCallback(LoadingCallback::class.java)
            pageNo = initPageNo
            mPresenter?.getShareData(pageNo,id)
        }.apply {
            SettingUtil.setLoadingColor(this@ShareByIdActivity, this)
            showCallback(LoadingCallback::class.java)
        }
        //初始化adapter
        adapter = AriticleAdapter(arrayListOf(), showTag = true, clickable = false).apply {
            if (SettingUtil.getListMode(this@ShareByIdActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@ShareByIdActivity))
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

            setOnItemClickListener { adapter, view, position ->
                launchActivity(Intent(this@ShareByIdActivity, WebviewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", this@ShareByIdActivity.adapter.data[position])
                        putString("tag", this@ShareByIdActivity::class.java.simpleName)
                        putInt("position", position)
                    })
                })
            }
        }
        floatbtn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(this@ShareByIdActivity)
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
            setColorSchemeColors(SettingUtil.getColor(this@ShareByIdActivity))
            setOnRefreshListener {
                //刷新
                pageNo = initPageNo
                mPresenter?.getShareData(pageNo,this@ShareByIdActivity.id)
            }
        }
        //初始化recyclerview
        footView = RecyclerViewUtils().initRecyclerView(this, swiperecyclerview, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getShareData(pageNo,id)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(this@ShareByIdActivity))
        }

        //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
        swiperecyclerview.run {
            adapter = this@ShareByIdActivity.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                @SuppressLint("RestrictedApi")
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(-1)) {
                        floatbtn.visibility = View.INVISIBLE
                    }
                    this@ShareByIdActivity.swipeRefreshLayout.isEnabled = recyclerView.childCount == 0 || recyclerView.getChildAt(0).top >= 0
                }
            })
        }
        //发起请求
        mPresenter?.getShareData(pageNo,id)
    }

    override fun requestDataSucces(shareResponse: ShareResponse) {
        share_name.text = shareResponse.coinInfo.username
        share_info.text = "积分 : ${shareResponse.coinInfo.coinCount}　排名 : ${shareResponse.coinInfo.rank}"
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

    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected, adapter.data[position].id).post()
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        //如果是登录了， 当前界面的数据与账户收藏集合id匹配的值需要设置已经收藏
        if (event.login) {
            event.collectIds.forEach {
                for (item in adapter.data) {
                    if (item.id == it.toInt()) {
                        item.collect = true
                        break
                    }
                }
            }
        } else {
            //退出了，把所有的收藏全部变为未收藏
            for (item in adapter.data) {
                item.collect = false
            }
        }
        adapter.notifyDataSetChanged()
    }

    /**
     * 接收到收藏文章的Event
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch {
            async {
                var indexResult = -1
                for (index in adapter.data.indices) {
                    if (adapter.data[index].id == event.id) {
                        adapter.data[index].collect = event.collect
                        indexResult = index
                        break
                    }
                }
                indexResult
            }.run {
                if (await() != -1) {
                    adapter.notifyItemChanged(await())
                }
            }
        }
    }

}
