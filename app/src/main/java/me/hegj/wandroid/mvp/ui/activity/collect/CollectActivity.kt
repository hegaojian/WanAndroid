package me.hegj.wandroid.mvp.ui.activity.collect

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.include_viewpager.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.weight.ScaleTransitionPagerTitleView
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.adapter.ViewPagerAdapter
import me.yokeyword.fragmentation.SupportFragment
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.UIUtil
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator


/**
 * 收藏
 * @Author:         hegaojian
 * @CreateDate:     2019/8/29 11:05
 */
class CollectActivity : BaseActivity<IPresenter>() {
    var mDataList = arrayListOf("文章", "网址")
    var fragments: MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter: ViewPagerAdapter? = null
    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_collect //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = ""
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        fragments.run {
            add(CollectAriticleFragment.newInstance())
            add(CollectUrlFragment.newInstance())
        }
        viewpager_linear.setBackgroundColor(SettingUtil.getColor(this))
        pagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments)
        view_pager.adapter = pagerAdapter
        val commonNavigator = CommonNavigator(this)
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


}
