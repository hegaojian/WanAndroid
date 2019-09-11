package me.hegj.wandroid.mvp.ui.activity.main.project

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jess.arms.di.component.AppComponent
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import kotlinx.android.synthetic.main.include_viewpager.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.ScaleTransitionPagerTitleView
import me.hegj.wandroid.app.weight.loadCallBack.ErrorCallback
import me.hegj.wandroid.app.weight.loadCallBack.LoadingCallback
import me.hegj.wandroid.di.component.main.project.DaggerProjectComponent
import me.hegj.wandroid.di.module.main.project.ProjectModule
import me.hegj.wandroid.mvp.contract.main.project.ProjectContract
import me.hegj.wandroid.mvp.model.entity.ClassifyResponse
import me.hegj.wandroid.mvp.presenter.main.project.ProjectPresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.adapter.ViewPagerAdapter
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import org.greenrobot.eventbus.Subscribe

/**
 * 项目
 */
class ProjectFragment : BaseFragment<ProjectPresenter>(), ProjectContract.View {

    var mDataList: MutableList<ClassifyResponse> = mutableListOf()
    var fragments: MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter: ViewPagerAdapter? = null
    lateinit var loadsir: LoadService<Any>

    companion object {
        fun newInstance(): ProjectFragment {
            val fragment = ProjectFragment()
            return fragment
        }
    }


    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerProjectComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .projectModule(ProjectModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val rootview = inflater.inflate(R.layout.fragment_viewpager, container, false)
        //绑定loadsir
        loadsir = LoadSir.getDefault().register(rootview.findViewById(R.id.view_pager)) {
            loadsir.showCallback(LoadingCallback::class.java)
            mPresenter?.getProjectTitles()
        }.apply {
            SettingUtil.setLoadingColor(_mActivity, this)
        }
        return rootview
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        viewpager_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        mPresenter?.getProjectTitles()//初始化的时候就请求数据，就是说一进入主页Activity这个就会请求
        // 不然 用户进来这个Fragment的时候再请求的话，界面头部啥都没有，这就不好看了
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        view_pager.adapter = pagerAdapter
        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mDataList[index].name
                    textSize = 17f
                    normalColor = Color.WHITE
                    selectedColor = Color.WHITE
                    setOnClickListener { view_pager.setCurrentItem(index, false) }
                }
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                return LinePagerIndicator(context).apply {
                    mode = LinePagerIndicator.MODE_EXACTLY
                    lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
                    lineWidth = UIUtil.dip2px(context, 30.0).toFloat()
                    roundRadius = UIUtil.dip2px(context, 6.0).toFloat()
                    startInterpolator = AccelerateInterpolator()
                    endInterpolator = DecelerateInterpolator(2.0f)
                    setColors(Color.WHITE)
                }
            }
        }
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, view_pager)
    }

    /**
     * 获取项目头部标题成功
     */
    override fun requestTitileSucc(titles: MutableList<ClassifyResponse>) {
        //请求到 项目分类头部标题集合
        if (titles.size == 0) {
            //没有数据，说明肯定出错了 这种情况只有第一次会出现，因为第一次请求成功后，会做本地保存操作，下次就算请求失败了，也会从本地取出缓存
            loadsir.showCallback(ErrorCallback::class.java)
        } else {
            loadsir.showSuccess()
            this.mDataList = titles
            if(fragments.size==0){
                //防止重复添加，出现 Can't change tag of fragment xxx bug
                //根据头部集合循环添加对应的Fragment
                for (i in titles.indices) {
                    fragments.add(ProjectChildFragment.newInstance(titles[i].id, 1))//分类项目页码从1开始
                }
                //在第一个添加 最新项目Fragment
                this.mDataList.add(0, ClassifyResponse(arrayListOf(), 0, 0, "最新项目", 0, 0, false, 0))
                fragments.add(0, ProjectChildFragment.newInstance(true, 0))//最新项目页码从0开始
            }
            //如果viewpager和 magicindicator 不为空的话，刷新他们 为空的话说明 用户还没有进来 这个Fragment
            pagerAdapter?.notifyDataSetChanged()
            magic_indicator?.navigator?.notifyDataSetChanged()
            view_pager?.offscreenPageLimit = fragments.size
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        viewpager_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        SettingUtil.setLoadingColor(_mActivity, loadsir)
    }

}
