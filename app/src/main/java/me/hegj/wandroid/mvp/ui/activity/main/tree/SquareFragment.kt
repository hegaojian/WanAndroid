package me.hegj.wandroid.mvp.ui.activity.main.tree

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.AddEvent
import me.hegj.wandroid.app.event.CollectEvent
import me.hegj.wandroid.app.event.LoginFreshEvent
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.RecyclerViewUtils
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.utils.SpaceItemDecoration
import me.hegj.wandroid.app.weight.DefineLoadMoreView
import me.hegj.wandroid.app.weight.loadCallBack.EmptyCallback
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.di.component.main.tree.DaggerSquareComponent
import me.hegj.wandroid.di.module.main.tree.SquareModule
import me.hegj.wandroid.mvp.contract.main.tree.SquareContract
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.AriticleResponse
import me.hegj.wandroid.mvp.presenter.main.tree.SquarePresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.main.tree.treeinfo.TreeInfoActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.AriticleAdapter
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe

/**  广场
  * @Author:         hegaojian
  * @CreateDate:     2019/10/8 16:52
 */
class SquareFragment : BaseFragment<SquarePresenter>(), SquareContract.View {
    private var initPageNo = 0 //注意，广场的页码是从0开始的！！！！！
    var pageNo = initPageNo //当前页码
    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: AriticleAdapter
    private var footView: DefineLoadMoreView? = null
    companion object {
        fun newInstance(): SquareFragment {
            return SquareFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerSquareComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .squareModule(SquareModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(rootview.findViewById(R.id.swipeRefreshLayout)) {
            loadsir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            mPresenter?.getSquareData(pageNo)
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
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
                mPresenter?.getSquareData(pageNo)
            }
        }
        floatbtn.run {
            backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
            setOnClickListener {
                val layoutManager = swiperecyclerview.layoutManager as LinearLayoutManager
                //如果当前recyclerview 最后一个视图位置的索引大于等于40，则迅速返回顶部，否则带有滚动动画效果返回到顶部
                if (layoutManager.findLastVisibleItemPosition() >= 40) {
                    //没有动画迅速返回到顶部(极快)
                    swiperecyclerview.scrollToPosition(0)
                } else {
                    //有滚动动画返回到顶部(有点慢)
                    swiperecyclerview.smoothScrollToPosition(0)
                }
            }
        }
        //初始化recyclerview
        footView = RecyclerViewUtils().initRecyclerView(_mActivity, swiperecyclerview, SwipeRecyclerView.LoadMoreListener {
            //加载更多
            mPresenter?.getSquareData(pageNo)
        }).apply {
            setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        }
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
        //初始化适配器
        adapter = AriticleAdapter(mutableListOf(),true).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            setOnItemClickListener { _, view, position ->
                launchActivity(Intent(_mActivity, WebviewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", this@SquareFragment.adapter.data[position])
                        putString("tag", this@SquareFragment::class.java.simpleName)
                        putInt("position", position)
                    })
                })
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        //设置适配器
        swiperecyclerview.adapter = adapter
        //设置加载中
        loadsir.showCallback(LoadingCallback::class.java)
        //请求数据
        mPresenter?.getSquareData(pageNo)
    }


    @SuppressLint("RestrictedApi")
    override fun requestDataSucc(apiPagerResponse: ApiPagerResponse<MutableList<AriticleResponse>>) {
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
            //页码不是0 说明是加载更多时出现的错误，设置recyclerview加载错误，
            swiperecyclerview.loadMoreError(0, errorMsg)
        }
    }

    /**
     * 收藏回调
     */
    override fun collect(collected: Boolean, position: Int) {
        CollectEvent(collected,adapter.data[position].id).post()
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
     * 在详情中收藏时，接收到EventBus
     */
    @Subscribe
    fun collectChange(event: CollectEvent) {
        //使用协程做耗时操作
        GlobalScope.launch{
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
                if(await()!=-1){
                    adapter.notifyItemChanged(await())
                }
            }
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        SettingUtil.setLoadingColor(_mActivity, loadsir)
        footView?.setLoadViewColor(SettingUtil.getOneColorStateList(_mActivity))
        if (SettingUtil.getListMode(_mActivity) != 0) {
            adapter.openLoadAnimation(SettingUtil.getListMode(_mActivity))
        } else {
            adapter.closeLoadAnimation()
        }
    }

    /**
     * 接收到添加了分享文章的通知
     */
    @Subscribe
    fun addEvent(event: AddEvent){
        if(event.code== AddEvent.SHARE_CODE ||event.code==AddEvent.DELETE_CODE){
            //刷新
            swipeRefreshLayout.isRefreshing = true
            pageNo = initPageNo
            mPresenter?.getSquareData(pageNo)
        }
    }

}
