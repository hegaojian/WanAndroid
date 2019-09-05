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
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.CollectEvent
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.utils.SpaceItemDecoration
import me.hegj.wandroid.app.weight.CollectView
import me.hegj.wandroid.app.weight.loadCallBack.EmptyCallback
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.di.component.collect.DaggerCollectUrlComponent
import me.hegj.wandroid.di.module.collect.CollectUrlModule
import me.hegj.wandroid.mvp.contract.collect.CollectUrlContract
import me.hegj.wandroid.mvp.model.entity.CollectUrlResponse
import me.hegj.wandroid.mvp.presenter.collect.CollectUrlPresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.CollectUrlAdapter
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe


class CollectUrlFragment : BaseFragment<CollectUrlPresenter>(), CollectUrlContract.View {
    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: CollectUrlAdapter

    companion object {
        fun newInstance(): CollectUrlFragment {
            return CollectUrlFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerCollectUrlComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .collectUrlModule(CollectUrlModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(rootview.findViewById(R.id.swipeRefreshLayout)) {
            loadsir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            mPresenter?.getCollectUrlData()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity,this)
        }
        return rootview
    }

    override fun initData(savedInstanceState: Bundle?) {
        //初始化swipeRefreshLayout
        swipeRefreshLayout.run {
            setColorSchemeColors(SettingUtil.getColor(_mActivity))
            //设置刷新监听回调
            setOnRefreshListener {
                mPresenter?.getCollectUrlData()
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
        swiperecyclerview.run {
            layoutManager = LinearLayoutManager(_mActivity)
            setHasFixedSize(true)
            //设置item的行间距
            addItemDecoration(SpaceItemDecoration(0, UIUtil.dip2px(_mActivity, 8.0)))
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
        adapter = CollectUrlAdapter(arrayListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            //点击爱心收藏执行操作
            setOnCollectViewClickListener(object : CollectUrlAdapter.OnCollectViewClickListener {
                override fun onClick(helper: BaseViewHolder, v: CollectView, position: Int) {
                    mPresenter?.uncollect(adapter.data[position].id, position)
                }
            })
            //点击了整行
            setOnItemClickListener { _, view, position ->
                val intent = Intent(_mActivity, WebviewActivity::class.java)
                val bundle = Bundle().apply {
                    putSerializable("collectUrl", adapter.data[position])
                    putString("tag", this@CollectUrlFragment::class.java.simpleName)
                    putInt("position", position)
                }
                intent.putExtras(bundle)
                startActivity(intent)
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        super.onLazyInitView(savedInstanceState)
        swiperecyclerview.adapter = adapter//设置适配器
        loadsir.showCallback(LoadingCallback::class.java)//设置加载中
        mPresenter?.getCollectUrlData()//请求数据
    }

    override fun requestDataUrlSucc(apiPagerResponse: MutableList<CollectUrlResponse>) {
        swipeRefreshLayout.isRefreshing = false
        if (apiPagerResponse.size == 0) {
            //如果没有数据，页面提示空布局
            loadsir.showCallback(EmptyCallback::class.java)
        } else {
            //有数据
            loadsir.showSuccess()
            adapter.setNewData(apiPagerResponse)
        }
    }

    override fun requestDataFaild(errorMsg: String) {
        swipeRefreshLayout.isRefreshing = false
        loadsir.setCallBack(ErrorCallback::class.java) { _, view ->
            //设置错误页文字错误提示
            view.findViewById<TextView>(R.id.error_text).text = errorMsg
            //设置错误
            loadsir.showCallback(ErrorCallback::class.java)
        }
    }

    override fun uncollect(position: Int) {
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
        swipeRefreshLayout.isRefreshing = true
        mPresenter?.getCollectUrlData()
    }
}
