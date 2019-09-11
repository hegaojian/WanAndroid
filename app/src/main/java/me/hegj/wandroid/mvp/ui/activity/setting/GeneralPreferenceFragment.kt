package me.hegj.wandroid.mvp.ui.activity.setting

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.color.colorChooser
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.jess.arms.integration.AppManager
import com.jess.arms.utils.ArmsUtils
import com.tencent.bugly.beta.Beta
import me.hegj.wandroid.R
import me.hegj.wandroid.app.event.LoginFreshEvent
import me.hegj.wandroid.app.event.SettingChangeEvent
import me.hegj.wandroid.app.utils.*
import me.hegj.wandroid.app.weight.IconPreference
import me.hegj.wandroid.mvp.model.entity.BannerResponse
import me.hegj.wandroid.mvp.ui.activity.start.LoginActivity
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity


class GeneralPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


    private var colorPreview: IconPreference? = null
    private lateinit var parentActivity: SettingActivity

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        parentActivity = activity as SettingActivity
        colorPreview = findPreference("color")
        setText()
        findPreference<Preference>("exit")?.isVisible = CacheUtil.isLogin()//未登录时，退出登录需要隐藏
        findPreference<Preference>("exit")?.setOnPreferenceClickListener { preference ->
            MaterialDialog(parentActivity).show {
                title(R.string.title)
                message(text = "确定退出登录吗？")
                positiveButton(text = "退出") {
                    LoginFreshEvent(false, arrayListOf()).post()
                    CacheUtil.setUser(null)
                    AppManager.getAppManager().startActivity(Intent(parentActivity, LoginActivity::class.java))
                    parentActivity.finish()
                }
                negativeButton(R.string.cancel)
            }
            false
        }

        findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
            MaterialDialog(parentActivity).show {
                title(R.string.title)
                message(text = "确定清除缓存吗？")
                positiveButton(text = "清除") {
                    CacheDataManager.clearAllCache(parentActivity)
                    ArmsUtils.snackbarText("清理成功")
                    setText()
                }
                negativeButton(R.string.cancel)
            }
            false
        }
        findPreference<Preference>("mode")?.setOnPreferenceClickListener {
            MaterialDialog(parentActivity).show {
                listItemsSingleChoice(R.array.setting_modes, initialSelection = SettingUtil.getListMode(parentActivity)) { dialog, index, text ->
                    SettingUtil.setListMode(parentActivity, index)
                    it.summary = text
                    //通知其他界面立马修改配置
                    SettingChangeEvent().post()
                }
                title(text = "设置列表动画")
                positiveButton(R.string.confirm)
                negativeButton(R.string.cancel)
            }
            false
        }
        findPreference<IconPreference>("color")?.setOnPreferenceClickListener {
            MaterialDialog(parentActivity).show {
                title(R.string.choose_theme_color)
                colorChooser(ColorUtil.ACCENT_COLORS, initialSelection = SettingUtil.getColor(parentActivity), subColors = ColorUtil.PRIMARY_COLORS_SUB) { dialog, color ->
                    SettingUtil.setColor(parentActivity, color)
                    //通知其他界面立马修改配置
                    SettingChangeEvent().post()
                }
                positiveButton(R.string.done)
                negativeButton(R.string.cancel)
            }
            false
        }

        findPreference<Preference>("version")?.setOnPreferenceClickListener {
            Beta.checkUpgrade(true, false)
            false
        }
        findPreference<Preference>("copyRight")?.setOnPreferenceClickListener {
            ShowUtils.showDialog(parentActivity, ArmsUtils.getString(activity, R.string.copyright_tip))
            false
        }
        findPreference<Preference>("author")?.setOnPreferenceClickListener {
            ShowUtils.showDialog(parentActivity, "扣　扣：824868922\n\n微　信：hgj840\n\n邮　箱：824868922@qq.com", "联系我")
            false
        }
        findPreference<Preference>("project")?.setOnPreferenceClickListener {
            val data = BannerResponse("", 0, "", 0, 0, "一位练习长达两年半的安卓练习生根据鸿神提供的WanAndroid开放Api来制作的产品级App", 0, findPreference<Preference>("project")?.summary.toString())
            parentActivity.launchActivity(Intent(parentActivity, WebviewActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putSerializable("bannerdata", data)
                })
            })
            false
        }
        findPreference<Preference>("open")?.setOnPreferenceClickListener {
            parentActivity.launchActivity(Intent(parentActivity, OpenProjectActivity::class.java))
            false
        }
    }

    /**
     * 初始化设值
     */
    private fun setText() {
        findPreference<CheckBoxPreference>("top")?.isChecked = SettingUtil.getRequestTop(parentActivity)
        findPreference<SwitchPreferenceCompat>("slidable")?.isChecked = SettingUtil.getSlidable(parentActivity)
        findPreference<Preference>("clearCache")?.summary = CacheDataManager.getTotalCacheSize(parentActivity)
        val version = "当前版本 " + parentActivity.packageManager.getPackageInfo(parentActivity.packageName, 0).versionName
        findPreference<Preference>("version")?.summary = version
        val modes = parentActivity.resources.getStringArray(R.array.setting_modes)
        findPreference<Preference>("mode")?.summary = modes[SettingUtil.getListMode(parentActivity)]
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == "color") {
            colorPreview?.setView()
        }
    }

}

