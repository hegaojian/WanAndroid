package me.hegj.wandroid.mvp.ui.activity.collect

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.CollectEvent
import me.hegj.wandroid.app.utils.RecyclerViewUtils
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.CollectView
import me.hegj.wandroid.app.weight.DefineLoadMoreView
import me.hegj.wandroid.app.weight.loadCallBack.EmptyCallback
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.di.component.collect.DaggerCollectComponent
import me.hegj.wandroid.di.module.collect.CollectModule
import me.hegj.wandroid.mvp.contract.collect.CollectContract
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.CollectResponse
import me.hegj.wandroid.mvp.presenter.collect.CollectPresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.CollectAdapter
import org.greenrobot.eventbus.Subscribe

/**
 * 收藏文章fragment
 * @Author:         hegaojian
 * @CreateDate:     2019/8/29 19:30
 */
class CollectAriticleFragment : BaseFragment<CollectPresenter>(), CollectContract.View {
    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: CollectAdapter
    var initPageNo = 0 //分页页码初始值 收藏文章列表是从0开始的
    var pageNo = initPageNo
    private var footView: DefineLoadMoreView? = null

    companion object {
        fun newInstance(): CollectAriticleFragment {
            return CollectAriticleFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerCollectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .collectModule(CollectModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(rootview.findViewById(R.id.swipeRefreshLayout)) {
            loadsir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            pageNo = initPageNo
            mPresenter?.getCollectDataByType(pageNo)
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return rootview
    }

    override fun initData(savedInstanceState: Bundle?) {
        //初始化swipeRefreshLayout
        swipeRefreshLayout.run {
            //设置颜色
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            //设置刷新监听回调
            setOnRefreshListener {
                pageNo = initPageNo
                mPresenter?.getCollectDataByType(pageNo)
            }
        }
        floatbtn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = swiperecyclerview.layoutManager as LinearLayoutManager
                //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    swiperecyclerview.scrollToPosition(0)//没有动画迅速返回到顶部(极快)
                } else {
                    swiperecyclerview.smoothScrollToPosition(0)//有滚动动画返回到顶部(有点慢)
                }
            }
        }
        //初始化recyclerview
        footView = RecyclerViewUtils().initRecyclerView(_mActivity, swiperecyclerview, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getCollectDataByType(pageNo)
        })

        //初始化recyclerview
        swiperecyclerview.run {
            layoutManager = LinearLayoutManager(_mActivity)
            setHasFixedSize(true)
            //监听recyclerview滑动到顶部的时候，需要把向上返回顶部的按钮隐藏
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
        //初始化adapter
        adapter = CollectAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            //点击爱心收藏执行操作
            setOnCollectViewClickListener(object : CollectAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    mPresenter?.uncollect(adapter.data[position].id, adapter.data[position].originId, position)
                }
            })
            //点击了整行
            setOnItemClickListener { _, view, position ->
                val intent = Intent(_mActivity, WebviewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("collect", adapter.data[position])
                    putString("tag", this@CollectAriticleFragment::class.java.simpleName)
                    putInt("position", position)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        swiperecyclerview.adapter = adapter//设置适配器
        loadsir.showCallback(LoadingCallback::class.java)//设置加载中
        mPresenter?.getCollectDataByType(pageNo)//请求数据
    }


    @SuppressLint("RestrictedApi")
    override fun requestDataSucc(apiPagerResponse: ApiPagerResponse<MutableList<CollectResponse>>) {
        swipeRefreshLayout.isRefreshing = false
        if (pageNo == initPageNo && apiPagerResponse.datas.size == 0) {
            //如果是第一页，并且没有数据，页面提示空布局
            loadsir.showCallback(EmptyCallback::class.java)
        } else if (pageNo == initPageNo) {
            loadsir.showSuccess()
            //如果是刷新的话，floatbutton就要隐藏了，因为这时候肯定是要在顶部的
            floatbtn.visibility = View.INVISIBLE
            adapter.setNewData(apiPagerResponse.datas)
        } else {
            //不是第一页 且有数据
            loadsir.showSuccess()
            adapter.addData(apiPagerResponse.datas)
        }
        pageNo++
        if (apiPagerResponse.pageCount >= pageNo) {
            //如果总条数大于等于当前页数时 还有更多数据
            swiperecyclerview.loadMoreFinish(false, true)
        } else {
            //没有更多数据
            swiperecyclerview.postDelayed({
                //解释一下为什么这里要延时0.2秒操作。。。
                //因为上面的adapter.addData(data) 数据刷新了适配器，是需要等待时间的，还没刷新完，这里就已经执行了没有更多数据
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
            //页码不是1 说明是加载更多时出现的错误，设置recyclerview加载错误，
            swiperecyclerview.loadMoreError(0, errorMsg)
        }
    }

    override fun uncollect(position: Int) {
        //通知点其他的页面刷新一下这个数据
        CollectEvent(false, adapter.data[position].originId, this::class.java.simpleName).post()
        //当前收藏数据大于1条的时候，直接删除
        if (adapter.data.size > 1) {
            adapter.remove(position)
        } else {
            //小于等于1条时，不要删除了，直接给界面设置成空数据
            loadsir.showCallback(EmptyCallback::class.java)
        }
    }

    override fun uncollectFaild(position: Int) {

        adapter.notifyItemChanged(position)
    }

    /**
     * 在详情中收藏时，接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //如果tag不是当前类名，需要刷新
        if (this::class.java.simpleName != event.tag) {
            swipeRefreshLayout.isRefreshing = true
            pageNo = initPageNo
            mPresenter?.getCollectDataByType(pageNo)
        }
    }
}