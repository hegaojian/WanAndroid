package me.hegj.wandroid.app.weight

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder

import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.SettingUtil


/**     
  * @Author:         hegaojian
  * @CreateDate:     2019/8/12 14:23
 */

class IconPreference(context: Context, attrs: AttributeSet) : Preference(context, attrs) {

     var circleImageView: MyColorCircleView? = null
    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder?) {
        super.onBindViewHolder(holder)
        val color = SettingUtil.getColor(context)
        circleImageView = holder?.itemView?.findViewById(R.id.iv_preview)
        circleImageView?.color = color
        circleImageView?.border = color
    }

    fun setView() {
        val color = SettingUtil.getColor(context)
        circleImageView?.color = color
        circleImageView?.border = color
    }
}