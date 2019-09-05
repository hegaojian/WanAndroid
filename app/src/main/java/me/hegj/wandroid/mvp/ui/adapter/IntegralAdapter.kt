package me.hegj.wandroid.mvp.ui.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.SettingUtil
import me.hegj.wandroid.mvp.model.entity.IntegralResponse

/**
 * 积分排行 adapter
 * @Author:         hegaojian
 * @CreateDate:     2019/9/1 9:52
 */
class IntegralAdapter(data: ArrayList<IntegralResponse>?) : BaseQuickAdapter<IntegralResponse, BaseViewHolder>(R.layout.item_integral, data) {
    private var rankNum: Int = -1

    constructor(data: ArrayList<IntegralResponse>?, rank: Int) : this(data) {
        this.rankNum = rank
    }

    override fun convert(helper: BaseViewHolder, item: IntegralResponse?) {
        //赋值
        item?.run {
            if(rankNum==helper.adapterPosition+1){
                helper.setTextColor(R.id.item_integral_rank,SettingUtil.getColor(mContext))
                helper.setTextColor(R.id.item_integral_name,SettingUtil.getColor(mContext))
                helper.setTextColor(R.id.item_integral_count,SettingUtil.getColor(mContext))
            }else{
                helper.setTextColor(R.id.item_integral_rank,ContextCompat.getColor(mContext,R.color.colorBlack333))
                helper.setTextColor(R.id.item_integral_name,ContextCompat.getColor(mContext,R.color.colorBlack666))
                helper.setTextColor(R.id.item_integral_count,ContextCompat.getColor(mContext,R.color.textHint))
            }
            helper.setText(R.id.item_integral_rank, "${helper.adapterPosition + 1}")
            helper.setText(R.id.item_integral_name, username)
            helper.setText(R.id.item_integral_count, coinCount.toString())
        }
    }
}


