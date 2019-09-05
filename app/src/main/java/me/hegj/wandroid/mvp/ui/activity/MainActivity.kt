package me.hegj.wandroid.mvp.ui.activity

import android.os.Bundle
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.ShowUtils
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.activity.main.MainFragment
import me.yokeyword.fragmentation.SupportFragment
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.greenrobot.eventbus.Subscribe


class MainActivity : BaseActivity<IPresenter>() {

    override fun setupActivityComponent(appComponent: AppComponent) {

    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_main //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    override fun initData(savedInstanceState: Bundle?) {
        if (findFragment(MainFragment::class.java) == null) {
            loadRootFragment(R.id.main_framelayout, MainFragment.newInstance())
        }

    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        // 设置横向(和安卓4.x动画相同)
        return DefaultHorizontalAnimator()
    }

    /**
     * 启动一个其他的Fragment
     */
    fun startBrotherFragment(targetFragment: SupportFragment) {
        start(targetFragment)
    }

    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        initStatusBar()
    }

    var exitTime: Long = 0

    override fun onBackPressedSupport() {
        if (System.currentTimeMillis() - this.exitTime > 2000L) {
            ShowUtils.showToast(this, "再按一次退出程序")
            this.exitTime = System.currentTimeMillis()
            return
        } else {
            super.onBackPressedSupport()
        }
    }


}
