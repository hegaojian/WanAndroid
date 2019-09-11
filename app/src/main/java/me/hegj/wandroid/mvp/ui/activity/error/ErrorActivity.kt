package me.hegj.wandroid.mvp.ui.activity.error

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.jess.arms.di.component.AppComponent
import com.jess.arms.mvp.IPresenter
import kotlinx.android.synthetic.main.activity_error.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.ShowUtils
import me.hegj.wandroid.mvp.ui.BaseActivity


class ErrorActivity : BaseActivity<IPresenter>() {
    override fun setupActivityComponent(appComponent: AppComponent) {
    }

    override fun initView(savedInstanceState: Bundle?): Int {
        return R.layout.activity_error
    }

    override fun initData(savedInstanceState: Bundle?) {
        toolbar.run {
            setSupportActionBar(this)
            title = "发生错误"
        }

        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        error_restart.setOnClickListener {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }
        error_sendError.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", CustomActivityOnCrash.getStackTraceFromIntent(intent))
            // 将ClipData内容放到系统剪贴板里。
            cm.primaryClip = mClipData
            ShowUtils.showToast(this, "已复制错误日志到粘贴板")
            ShowUtils.showDialog(this, "扣　扣：824868922\n\n微　信：hgj840\n\n邮　箱：824868922@qq.com", "联系我")
        }
    }
}