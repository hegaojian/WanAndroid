package me.hegj.wandroid.mvp.ui.activity.main.me

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.Preference
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
import me.hegj.wandroid.mvp.model.entity.BannerResponse
import me.hegj.wandroid.mvp.model.entity.IntegralResponse
import me.hegj.wandroid.mvp.model.entity.UserInfoResponse
import me.hegj.wandroid.mvp.presenter.main.me.MePresenter
import me.hegj.wandroid.mvp.ui.BaseFragment
import me.hegj.wandroid.mvp.ui.activity.collect.CollectActivity
import me.hegj.wandroid.mvp.ui.activity.integral.IntegralActivity
import me.hegj.wandroid.mvp.ui.activity.setting.SettingActivity
import me.hegj.wandroid.mvp.ui.activity.share.ShareListActivity
import me.hegj.wandroid.mvp.ui.activity.start.LoginActivity
import me.hegj.wandroid.mvp.ui.activity.todo.TodoActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import org.greenrobot.eventbus.Subscribe
import android.net.Uri
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils


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
            title = ""
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
        ArmsUtils.obtainAppComponentFromContext(_mActivity).imageLoader().loadImage(_mActivity.applicationContext,
                ImageConfigImpl
                        .builder()
                        .url("https://avatars2.githubusercontent.com/u/18655288?s=460&v=4")
                        .imageView(imageView)
                        .errorPic(R.drawable.ic_account)
                        .fallback(R.drawable.ic_account)
                        .placeholder(R.drawable.ic_account)
                        .isCrossFade(true)
                        .isCircle(true)
                        .build())
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        if (CacheUtil.isLogin()) {
            //如果登录了 赋值，并且请求积分接口
            userInfo = CacheUtil.getUser()
            me_name.text = if(userInfo.nickname.isEmpty()) userInfo.username else userInfo.nickname
            me_swipe.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //没登录，就不要去请求积分接口了
            me_name.text = "请先登录~"
            me_info.text = "id : --　排名 : --"
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
            me_name.text = if(userInfo.nickname.isEmpty()) userInfo.username else userInfo.nickname
            //吊起请求 设置触发 下拉 swipe
            me_swipe.isRefreshing = true
            mPresenter?.getIntegral()
        } else {
            //接受到退出登录了，赶紧清空赋值
            me_name.text = "请先登录~"
            me_integral.text = "0"
            me_info.text = "id : --　排名 : --"
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

    @OnClick(R.id.me_setting, R.id.me_collect, R.id.me_linear, R.id.me_todo, R.id.me_integralLinear
            , R.id.me_article,R.id.me_join,R.id.me_about)
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
                } else {
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
                                putSerializable("integral", it)
                            })
                        }
                    })
                }
            }
            R.id.me_article -> {
                if (!CacheUtil.isLogin()) {
                    launchActivity(Intent(_mActivity, LoginActivity::class.java))
                } else {
                    launchActivity(Intent(_mActivity,ShareListActivity::class.java))
                }
            }
            R.id.me_about ->{
                val data = BannerResponse("", 0, "", 0, 0, "玩Android网站", 0, "https://www.wanandroid.com/")
                launchActivity(Intent(_mActivity, WebviewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("bannerdata", data)
                    })
                })
            }
            R.id.me_join -> {
                joinQQGroup("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D")
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
        me_info.text = "id : ${integral.userId}　排名 : ${integral.rank}"
        me_integral.text = integral.coinCount.toString()
    }

    /**
     * 获取积分失败回调
     */
    override fun getIntegralFaild(errorMsg: String) {
        me_swipe.isRefreshing = false
        ShowUtils.showToast(_mActivity, errorMsg)
    }

    /**
     * 加入qq聊天群
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            ShowUtils.showToast(_mActivity,"未安装手机QQ或安装的版本不支持")
            false
        }

    }
}
