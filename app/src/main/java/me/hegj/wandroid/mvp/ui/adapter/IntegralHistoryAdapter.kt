package me.hegj.wandroid.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.DatetimeUtil
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.mvp.model.entity.IntegralHistoryResponse
import java.util.*
import kotlin.collections.ArrayList

/**
 * 积分获取历史 adapter
 * @Author:         hegaojian
 * @CreateDate:     2019/9/1 12:21
 */
class IntegralHistoryAdapter(data: ArrayList<IntegralHistoryResponse>?) : BaseQuickAdapter<IntegralHistoryResponse, BaseViewHolder>(R.layout.item_integral_history, data) {
    override fun convert(helper: BaseViewHolder, item: IntegralHistoryResponse?) {
        //赋值
        item?.run {
            val descStr =if(desc.contains("积分")) desc.subSequence(desc.indexOf("积分"),desc.length) else ""
            helper.setText(R.id.item_integralhistory_title,reason+descStr )
            helper.setText(R.id.item_integralhistory_date, DatetimeUtil.formatDate(date,DatetimeUtil.DATE_PATTERN_SS))
            helper.setText(R.id.item_integralhistory_count, "+$coinCount")
            helper.setTextColor(R.id.item_integralhistory_count, SettingUtil.getColor(mContext))
        }
    }
}


