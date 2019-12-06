package me.hegj.wandroid.app.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kingja.loadsir.core.LoadService
import me.hegj.wandroid.app.weight.DefineLoadMoreView
import me.hegj.wandroid.mvp.ui.activity.main.me.MeFragment
import me.hegj.wandroid.mvp.ui.activity.setting.SettingActivity
import me.hegj.wandroid.mvp.ui.activity.start.LoginActivity
import me.yokeyword.fragmentation.SupportFragment

/**
 * 根据控件的类型设置主题，注意，控件具有优先级， 基本类型的控件建议放到最后，像 Textview，FragmentLayout，不然会出现问题，
 * 列如下面的BottomNavigationViewEx他的顶级父控件为FragmentLayout，如果先 is Fragmentlayout判断在 is BottomNavigationViewEx上面
 * 那么就会直接去执行 is FragmentLayout的代码块 跳过 is BottomNavigationViewEx的代码块了
 */
fun setUiTheme(context: Context, anylist: List<Any>) {
    anylist.forEach {
        when (it) {
            is LoadService<*> -> SettingUtil.setLoadingColor(context, it as LoadService<Any>)
            is FloatingActionButton -> it.backgroundTintList = SettingUtil.getOneColorStateList(context)
            is SwipeRefreshLayout -> it.setColorSchemeColors(SettingUtil.getColor(context))
            is DefineLoadMoreView -> it.setLoadViewColor(SettingUtil.getOneColorStateList(context))
            is BaseQuickAdapter<*, *> -> {
                if (SettingUtil.getListMode(context) != 0) {
                    it.openLoadAnimation(SettingUtil.getListMode(context))
                } else {
                    it.closeLoadAnimation()
                }
            }
            is BottomNavigationViewEx ->{
                it.itemIconTintList = SettingUtil.getColorStateList(context)
                it.itemTextColor = SettingUtil.getColorStateList(context)
            }
            is Toolbar -> it.setBackgroundColor(SettingUtil.getColor(context))
            is TextView -> it.setTextColor(SettingUtil.getColor(context))
            is LinearLayout -> it.setBackgroundColor(SettingUtil.getColor(context))
            is ConstraintLayout -> it.setBackgroundColor(SettingUtil.getColor(context))
            is FrameLayout -> it.setBackgroundColor(SettingUtil.getColor(context))
        }
    }
}
    fun Fragment.startActivityKx(cls :Class<*>,isNeedLogin:Boolean = false,bundle: Bundle = Bundle()){
        if(isNeedLogin){
            if (!CacheUtil.isLogin()) {
                startActivity(Intent(this.activity,LoginActivity::class.java))
            }else{
                startActivity(Intent(this.activity,cls).apply {
                    putExtras(bundle)
                })
            }
        }else{
            startActivity(Intent(this.activity,cls).apply {
                putExtras(bundle)
            })
        }
    }
    fun Activity.startActivityKx(cls :Class<*>, isNeedLogin:Boolean = false,bundle: Bundle = Bundle()){
        if(isNeedLogin){
            if (!CacheUtil.isLogin()) {
                startActivity(Intent(this,LoginActivity::class.java))
            }else{
                startActivity(Intent(this,cls).apply {
                    putExtras(bundle)
                })
            }
        }else{
            startActivity(Intent(this,cls).apply {
                putExtras(bundle)
            })
        }

    }
