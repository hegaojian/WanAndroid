package me.hegj.wandroid.mvp.ui.activity.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.fragment_main.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.di.component.DaggerMainComponent
import me.hegj.wandroid.di.module.MainModule
import me.hegj.wandroid.mvp.contract.MainContract
import me.hegj.wandroid.mvp.presenter.MainPresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.main.home.HomeFragment
import me.hegj.wandroid.mvp.ui.activity.main.me.MeFragment
import me.hegj.wandroid.mvp.ui.activity.main.project.ProjectFragment
import me.hegj.wandroid.mvp.ui.activity.main.publicNumber.PublicFragment
import me.hegj.wandroid.mvp.ui.activity.main.tree.TreeFragment
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.Subscribe


/**
 *主页
 */
class MainFragment : BaseFragment<MainPresenter>(), MainContract.View {
    private val first  = 0
    private val two    = 1
    private val three  = 2
    private val four   = 3
    private val five   = 4
    private val mFragments = arrayOfNulls<SupportFragment>(5)

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(MainModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        val homeFragment = findChildFragment(HomeFragment::class.java)
        if (homeFragment == null) {
            mFragments[first] = HomeFragment.newInstance()//主页
            mFragments[two]   = ProjectFragment.newInstance()//项目
            mFragments[three] = TreeFragment.newInstance()//体系
            mFragments[four]  = PublicFragment.newInstance()//公众号
            mFragments[five]  = MeFragment.newInstance()//我的
            loadMultipleRootFragment(R.id.main_frame, first, mFragments[first]
                    , mFragments[two], mFragments[three], mFragments[four], mFragments[five])
        } else {
            mFragments[first] = homeFragment
            mFragments[two]   = findChildFragment(ProjectFragment::class.java)
            mFragments[three] = findChildFragment(TreeFragment::class.java)
            mFragments[four]  = findChildFragment(PublicFragment::class.java)
            mFragments[five]  = findChildFragment(MeFragment::class.java)
        }
        main_bnve.run {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
            itemIconTintList = SettingUtil.getColorStateList(_mActivity)
            itemTextColor = SettingUtil.getColorStateList(_mActivity)
            setIconSize(20F, 20F)
            setTextSize(12F)
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_main -> showHideFragment(mFragments[first])
                    R.id.menu_project -> showHideFragment(mFragments[two])
                    R.id.menu_system -> showHideFragment(mFragments[three])
                    R.id.menu_public -> showHideFragment(mFragments[four])
                    R.id.menu_me -> showHideFragment(mFragments[five])
                }
                true
            }
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        main_bnve?.run {
            itemIconTintList = SettingUtil.getColorStateList(_mActivity)
            itemTextColor = SettingUtil.getColorStateList(_mActivity)
        }
    }
}
