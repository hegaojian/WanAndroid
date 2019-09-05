package me.hegj.wandroid.mvp.ui.activity.main.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.OnClick
import com.jess.arms.di.component.AppComponent
import kotlinx.android.synthetic.main.fragment_me.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.LoginFreshEvent
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.CacheUtil
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.utils.ShowUtils
import me.hegj.wandroid.di.component.main.me.DaggerMeComponent
import me.hegj.wandroid.di.module.main.me.MeModule
import me.hegj.wandroid.mvp.contract.main.me.MeContract
import me.hegj.wandroid.mvp.model.entity.IntegralResponse
import me.hegj.wandroid.mvp.model.entity.UserInfoResponse
import me.hegj.wandroid.mvp.presenter.main.me.MePresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.collect.CollectActivity
import me.hegj.wandroid.mvp.ui.activity.integral.IntegralActivity
import me.hegj.wandroid.mvp.ui.activity.setting.SettingActivity
import me.hegj.wandroid.mvp.ui.activity.start.LoginActivity
import me.hegj.wandroid.mvp.ui.activity.todo.TodoActivity
import org.greenrobot.eventbus.Subscribe

/**
 * 我的
 */
class MeFragment : BaseFragment<MePresenter>(), MeContract.View {


    private lateinit var userInfo: UserInfoResponse
    var integral: IntegralResponse? = null

    companion object {
        fun newInstance(): MeFragment {
            return MeFragment()
        }
    }

    override fun setupFragmentComponent(appComponent: AppComponent) {
        DaggerMeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .meModule(MeModule(this))
                .build()
                .inject(this)
    }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_me, container, false)
    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        toolbar.run {
            title = "我的"
        }
        me_swipe.run {
            setOnRefreshListener {
                //刷新积分
                mPresenter?.getIntegral()
            }
        }
        me_swipe.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        toolbar.setBackgroundColor(SettingUtil.getColor(_mActivity))
        me_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        me_integral.setTextColor(SettingUtil.getColor(_mActivity))
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (CacheUtil.isLogin()) {
            //如果登录了 赋值，并且请求积分接口
            userInfo = CacheUtil.getUser()
            me_name.text = userInfo.username
            me_swipe.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //没登录，就不要去请求积分接口了
            me_name.text = "去登录"
            me_integral.text = "0"
        }
    }

    /**
     * 接收到登录或退出的EventBus 刷新数据
     */
    @Subscribe
    fun freshLogin(event: LoginFreshEvent) {
        if (event.login) {
            //接收到登录了，赋值 并去请求积分接口
            userInfo = CacheUtil.getUser()
            me_name.text = userInfo.username
            //吊起请求 设置触发 下拉 swipe
            me_swipe.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //接受到退出登录了，赶紧清空赋值
            me_name.text = "去登录"
            me_integral.text = "0"
        }
    }

    /**
     * 接收到event时，重新设置当前界面控件的主题颜色和一些其他配置
     */
    @Subscribe
    fun settingEvent(event: SettingChangeEvent) {
        me_swipe.setColorSchemeColors(SettingUtil.getColor(_mActivity))
        toolbar.setBackgroundColor(SettingUtil.getColor(_mActivity))
        me_linear.setBackgroundColor(SettingUtil.getColor(_mActivity))
        me_integral.setTextColor(SettingUtil.getColor(_mActivity))
    }

    @OnClick(R.id.me_setting, R.id.me_collect, R.id.me_linear, R.id.me_todo, R.id.me_integralLinear)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.me_linear -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                }
            }
            R.id.me_collect -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                } else {
                    launchActivity(Intent(_mActivity, CollectActivity::class.java))
                }
            }
            R.id.me_todo -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                }else{
                    launchActivity(Intent(_mActivity, TodoActivity::class.java))
                }
            }
            R.id.me_integralLinear -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                } else {
                    launchActivity(Intent(_mActivity, IntegralActivity::class.java).apply {
                        integral?.let {
                            putExtras(Bundle().apply {
                                putSerializable("integral",it)
                            })
                        }
                    })
                }
            }
            R.id.me_setting -> {
                launchActivity(Intent(_mActivity, SettingActivity::class.java))
            }
        }
    }

    /**
     * 获取积分成功回调
     */
    override fun getIntegralSucc(integral: IntegralResponse) {
        this.integral = integral
        me_swipe.isRefreshing = false
        me_integral.text = integral.coinCount.toString()
    }

    /**
     * 获取积分失败回调
     */
    override fun getIntegralFaild(errorMsg: String) {
        me_swipe.isRefreshing = false
        ShowUtils.showToast(_mActivity, errorMsg)
    }
}
