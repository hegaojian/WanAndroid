package me.hegj.wandroid.mvp.ui.activity.main.tree.treeinfo

import android.annotation.SuppressLint
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
import me.hegj.wandroid.mvp.model.entity.SystemResponse
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
 * 主页体系-根据类型查询体系结果的activity
 * @Author:         hegaojian
 * @CreateDate:     2019/8/21 22:11
 */
@SuppressLint("Registered")
class TreeInfoActivity : BaseActivity<IPresenter>() {

    var position = 0//从上级体系中，点击tag的索引
    lateinit var systemResponse: SystemResponse//从上级体系中得到的data
    var fragments: MutableList<SupportFragment> = mutableListOf()
    internal var pagerAdapter: ViewPagerAdapter? = null

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_treeinfo
    }

    override fun initData(savedInstanceState: Bundle?) {
        intent.run {
            systemResponse = getSerializableExtra("data") as SystemResponse
            position = getIntExtra("position", 0)
        }
        toolbar.run {
            setSupportActionBar(this)
            title = systemResponse.name
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        //根据集合循环添加Fragment
        for (i in systemResponse.children.indices) {
            fragments.add(TreeinfoFragment.newInstance(systemResponse.children[i].id))
        }
        viewpager_linear?.setBackgroundColor(SettingUtil.getColor(this))
        pagerAdapter = ViewPagerAdapter(supportFragmentManager, fragments)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return systemResponse.children.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                return ScaleTransitionPagerTitleView(context).apply {
                    text = systemResponse.children[index].name
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
        view_pager.adapter = pagerAdapter
        view_pager.offscreenPageLimit = fragments.size
        view_pager.setCurrentItem(position, false)
    }
}