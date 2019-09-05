package me.hegj.wandroid.mvp.ui.activity.main.tree

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.utils.SpaceItemDecoration
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.di.component.main.tree.DaggerSystemComponent
import me.hegj.wandroid.di.module.main.tree.SystemModule
import me.hegj.wandroid.mvp.contract.main.tree.SystemContract
import me.hegj.wandroid.mvp.model.entity.SystemResponse
import me.hegj.wandroid.mvp.presenter.main.tree.SystemPresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.main.tree.treeinfo.TreeInfoActivity
import me.hegj.wandroid.mvp.ui.adapter.SystemAdapter
import net.lucode.hackware.magicindicator.buildins.UIUtil
import org.greenrobot.eventbus.Subscribe

/**
 * 体系
 */

class SystemFragment : BaseFragment<SystemPresenter>(), SystemContract.View {
    lateinit var loadsir: LoadService<Any>
    lateinit var adapter: SystemAdapter

    companion object {
        fun newInstance(): SystemFragment {
            val fragment = SystemFragment()
            return fragment
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerSystemComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .systemModule(SystemModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_list, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(rootview.findViewById(R.id.swipeRefreshLayout)) {
            loadsir.showCallback(LoadingCallback::class.java)
            //点击重试时请求
            mPresenter?.getSystemData()
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
                mPresenter?.getSystemData()
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
        //初始化适配器
        adapter = SystemAdapter(mutableListOf()).apply {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
            setOnItemClickListener { _, view, position ->
                launchActivity(Intent(_mActivity, TreeInfoActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("data", adapter.data[position])
                        putInt("position", 0)
                    })
                })
            }
            //设置点击tag的回调
            setTagClickListener(object : SystemAdapter.TagClicklistener {
                override fun onClick(position: Int, childPosition: Int) {
                    // position = 点击了第几个item, childPosition 点击的第几个tag
                    launchActivity(Intent(_mActivity, TreeInfoActivity::class.java).apply {
                        putExtras(Bundle().apply {
                            putSerializable("data", adapter.data[position])
                            putInt("position", childPosition)
                        })
                    })
                }
            })
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        swiperecyclerview.adapter = adapter//设置适配器
        loadsir.showCallback(LoadingCallback::class.java)//设置加载中
        mPresenter?.getSystemData()//请求数据
    }

    /**
     * 获取体系数据回调
     */
    override fun getSystemDataSucc(data: MutableList<SystemResponse>) {
        floatbtn.visibility = View.INVISIBLE
        if (data.size == 0) {
            //集合大小为0 说明肯定是第一次请求数据并且请求失败了，因为只要请求成功过一次就会有缓存数据
            loadsir.showCallback(ErrorCallback::class.java)
        } else {
            swipeRefreshLayout.isRefreshing = false
            loadsir.showCallback(SuccessCallback::class.java)
            adapter.setNewData(data)
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        floatbtn.backgroundTintList = SettingUtil.getOneColorStateList(_mActivity)
        SettingUtil.setLoadingColor(_mActivity, loadsir)
        swipeRefreshLayout.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        adapter.run {
            if (SettingUtil.getListMode(_mActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(_mActivity))
            } else {
                closeLoadAnimation()
            }
        }
    }

}
