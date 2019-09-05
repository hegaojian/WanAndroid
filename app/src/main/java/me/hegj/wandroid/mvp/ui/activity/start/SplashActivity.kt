package me.hegj.wandroid.mvp.ui.activity.start

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.activity.MainActivity

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SplashActivity : BaseActivity<IPresenter>() {
    private var alphaAnimation: AlphaAnimation? = null

    override fun setupActivityComponent(appComponent: AppComponent) {}

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_welcome
    }

    override fun initData(savedInstanceState: Bundle?) {
        //防止出现按Home键回到桌面时，再次点击重新进入该界面bug
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT !== 0) {
            finish()
            return
        }
        toolbar.run {
            setSupportActionBar(this)
            title = ""
        }
        welcome_base.setBackgroundColor(SettingUtil.getColor(this))
        //做一个1秒的透明度动画，没卵用。。。后面透明度被我改成一样了，直接当做计时器了
        alphaAnimation = AlphaAnimation(1.0F, 1.0F)
        alphaAnimation?.run {
            duration = 1000
            setAnimationListener(object : Animation.AnimationListener {

                override fun onAnimationRepeat(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    goToMainActivity()
                }

                override fun onAnimationStart(p0: Animation?) {}
            })
        }
        welcome_base.startAnimation(alphaAnimation)
    }

    fun goToMainActivity() {
        //跳转到主页
        launchActivity(Intent(this, MainActivity::class.java))
        finish()
        //带点渐变动画
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        alphaAnimation?.cancel()
        alphaAnimation = null

    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(this)))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = SettingUtil.getColor(this)
        }
        toolbar?.setBackgroundColor(SettingUtil.getColor(this))
    }
}