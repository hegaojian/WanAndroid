package me.hegj.wandroid.mvp.ui.activity

import android.os.Bundle
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import com.tencent.bugly.beta.Beta
import me.hegj.wandroid.BuildConfig
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
        //进入首页检查更新
        Beta.checkUpgrade(false, true)

        //如果你导入了该项目并打算以该项目为基础编写自己的项目，看到了这段代码，请记得更换 Bugly Key！
        //Bugly网址：https://bugly.qq.com/v2/index ，具体修改请看 https://github.com/hegaojian/WanAndroid/issues/7
        if(BuildConfig.APPLICATION_ID != "me.hegj.wandroid"&&BuildConfig.BUGLY_KEY =="5a5f6366fc"){
            showMessage("请更换Bugly Key！防止产生的错误信息反馈到作者账号上，具体请查看app模块中的 build.gradle文件，修改BUGLY_KEY字段值为自己在Bugly官网申请的Key")
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
