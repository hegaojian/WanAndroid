package me.hegj.wandroid.mvp.ui.activity.main.tree

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import kotlinx.android.synthetic.main.include_viewpager.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.ScaleTransitionPagerTitleView
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
 * 体系
 */

class TreeFragment : BaseFragment<IPresenter>() {
    var mDataList = arrayListOf("体系", "导航")
    var fragments: MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter: ViewPagerAdapter? = null

    companion object {
        fun newInstance(): TreeFragment {
            return TreeFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {

    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_viewpager, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        fragments.run {
            add(SystemFragment.newInstance())
            add(NavigationFragment.newInstance())
        }
        viewpager_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        pagerAdapter = ViewPagerAdapter(childFragmentManager, fragments)
        view_pager.adapter = pagerAdapter
        val commonNavigator = CommonNavigator(_mActivity)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = mDataList[index]
                    textSize = 18f
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

    override fun onResume() {
        super.onResume()
        viewpager_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        viewpager_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
    }
}
