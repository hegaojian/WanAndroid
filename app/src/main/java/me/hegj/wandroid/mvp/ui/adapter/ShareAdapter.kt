package me.hegj.wandroid.mvp.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.hegj.wandroid.R
import me.hegj.wandroid.mvp.model.entity.AriticleResponse


/**
 * 分享的文章 adapter
 * @Author:         hegaojian
 * @CreateDate:     2019/9/1 9:52
 */
class ShareAdapter(data: ArrayList<AriticleResponse>?) : BaseQuickAdapter<AriticleResponse, BaseViewHolder>(R.layout.item_share_ariticle, data) {

    override fun convert(helper: BaseViewHolder, item: AriticleResponse?) {
        //赋值
        item?.run {
            helper.setText(R.id.item_share_title, title)
            helper.setText(R.id.item_share_date, niceDate)
            helper.addOnClickListener(R.id.item_share_del)
        }
    }
}


