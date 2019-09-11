package me.hegj.wandroid.mvp.ui.activity.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import kotlinx.android.synthetic.main.activity_openproject.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.app.utils.SpaceItemDecoration
import me.hegj.wandroid.mvp.model.entity.BannerResponse
import me.hegj.wandroid.mvp.model.entity.OpenProject
import me.hegj.wandroid.mvp.ui.BaseActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity
import me.hegj.wandroid.mvp.ui.adapter.OpenProjectAdapter
import net.lucode.hackware.magicindicator.buildins.UIUtil

@SuppressLint("Registered")
class OpenProjectActivity : BaseActivity<IPresenter>() {
    lateinit var openAdapter: OpenProjectAdapter
    var openData: ArrayList<OpenProject> = arrayListOf()
    override fun setupActivityComponent(appComponent: AppComponent) {}

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_openproject
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "开源项目"
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { finish() }
        }
        getOpenData()
        openAdapter = OpenProjectAdapter(openData).apply {
            if (SettingUtil.getListMode(this@OpenProjectActivity) != 0) {
                openLoadAnimation(SettingUtil.getListMode(this@OpenProjectActivity))
            } else {
                closeLoadAnimation()
            }
            setOnItemClickListener { adapter, view, position ->
                val data = BannerResponse("", 0, "", 0, 0, openData[position].name, 0, openData[position].url)
                launchActivity(Intent(this@OpenProjectActivity, WebviewActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putSerializable("bannerdata", data)
                    })
                })
            }
        }
        openproject_recycler.apply {
            layoutManager = LinearLayoutManager(this@OpenProjectActivity)
            //设置item的行间距
            addItemDecoration(SpaceItemDecoration(0, UIUtil.dip2px(this@OpenProjectActivity, 8.0)))
            setHasFixedSize(true)
            adapter = openAdapter
        }
    }

    fun getOpenData() {
        openData.add(OpenProject("Kotlin", "The Kotlin Programming Language", "https://github.com/JetBrains/kotlin"))
        openData.add(OpenProject("RxJava", "RxJava – Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.", "https://github.com/ReactiveX/RxJava"))
        openData.add(OpenProject("Dagger", "A fast dependency injector for Android and Java.", "https://github.com/square/dagger"))
        openData.add(OpenProject("Okhttp", "An HTTP client for Android, Kotlin, and Java.", "https://github.com/square/okhttp"))
        openData.add(OpenProject("Retrofit", "Type-safe HTTP client for Android and Java by Square, Inc.", "https://github.com/square/retrofit"))
        openData.add(OpenProject("Gson", "A Java serialization/deserialization library to convert Java Objects into JSON and back", "https://github.com/google/gson"))
        openData.add(OpenProject("MVPArms", "A common architecture for Android applications developing based on MVP, integrates many open source projects, to make your developing quicker and easier (一个整合了大量主流开源项目高度可配置化的 Android MVP 快速集成框架).", "https://github.com/JessYanCoding/MVPArms"))
        openData.add(OpenProject("AndroidAutoSize", "A low-cost Android screen adaptation solution (今日头条屏幕适配方案终极版，一个极低成本的 Android 屏幕适配方案).", "https://github.com/JessYanCoding/AndroidAutoSize"))
        openData.add(OpenProject("Eventbus", "Event bus for Android and Java that simplifies communication between Activities, Fragments, Threads, Services, etc. Less code, better quality", "https://github.com/greenrobot/EventBus"))
        openData.add(OpenProject("Material-dialogs", "A beautiful, fluid, and extensible dialogs API for Kotlin & Android", "https://github.com/afollestad/material-dialogs"))
        openData.add(OpenProject("Fragmentation", "A powerful library that manage Fragment for Android!", "https://github.com/YoKeyword/Fragmentation"))
        openData.add(OpenProject("BottomNavigationViewEx", "An android lib for enhancing BottomNavigationView. 一个增强BottomNavigationView的安卓库。", "https://github.com/ittianyu/BottomNavigationViewEx"))
        openData.add(OpenProject("MagicIndicator", "A powerful, customizable and extensible ViewPager indicator framework. As the best alternative of ViewPagerIndicator, TabLayout and PagerSlidingTabStrip —— 强大、可定制、易扩展的 ViewPager 指示器框架。是ViewPagerIndicator、TabLayout、PagerSlidingTabStrip的最佳替代品。支持角标，更支持在非ViewPager场景下使用（使用hide()、show()切换Fragment或使用setVisibility切换FrameLayout里的View等）", "https://github.com/hackware1993/MagicIndicator"))
        openData.add(OpenProject("BGABanner-Android", "引导界面滑动导航 + 大于等于1页时无限轮播 + 各种切换动画轮播效果", "https://github.com/bingoogolapple/BGABanner-Android"))
        openData.add(OpenProject("BaseRecyclerViewAdapterHelper", "BRVAH:Powerful and flexible RecyclerAdapter", "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"))
        openData.add(OpenProject("SwipeRecyclerView", "RecyclerView侧滑菜单，Item拖拽，滑动删除Item，自动加载更多，HeaderView，FooterView，Item分组黏贴。", "https://github.com/yanzhenjie/SwipeRecyclerView"))
        openData.add(OpenProject("RevealLayout", "揭示效果布局，可以指定2个子布局，以圆形揭示效果切换选中状态 ", "https://github.com/goweii/RevealLayout"))
        openData.add(OpenProject("Loadsir", "A lightweight, good expandability Android library used for displaying different pages like loading, error, empty, timeout or even your custom page when you load a page.(优雅地处理加载中，重试，无数据等)", "https://github.com/KingJA/LoadSir"))
        openData.add(OpenProject("Agentweb", "AgentWeb is a powerful library based on Android WebView", "https://github.com/Justson/AgentWeb"))
        openData.add(OpenProject("FlowLayout", "Android流式布局，支持单选、多选等，适合用于产品标签等。", "https://github.com/hongyangAndroid/FlowLayout"))
        openData.add(OpenProject("MMKV", "An efficient, small mobile key-value storage framework developed by WeChat. Works on iOS, Android, macOS and Windows.", "https://github.com/Tencent/MMKV"))
        openData.add(OpenProject("SmartSwipe", "An android library to make swipe more easier and more powerful. 关于侧滑，有这一个就够了", "https://github.com/luckybilly/SmartSwipe"))
        openData.add(OpenProject("CustomActivityOnCrash", "Android库允许在应用崩溃时启动自定义活动，而不是显示讨厌的“不幸的是，X已经停止”对话框。", "https://github.com/Ereza/CustomActivityOnCrash"))
    }
}