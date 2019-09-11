package me.hegj.wandroid.mvp.ui.adapter

import android.text.Html
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.util.MultiTypeDelegate
import com.jess.arms.http.imageloader.glide.ImageConfigImpl
import com.jess.arms.utils.ArmsUtils
import me.hegj.wandroid.R
import me.hegj.wandroid.app.weight.CollectView
import me.hegj.wandroid.mvp.model.entity.AriticleResponse


class AriticleAdapter(data: ArrayList<AriticleResponse>?) : BaseQuickAdapter<AriticleResponse, BaseViewHolder>(data) {
    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null
    private val Ariticle = 1//文章类型
    private val Project = 2//项目类型 本来打算不区分文章和项目布局用统一布局的，但是布局完以后发现差异化蛮大的，所以还是分开吧
    private var showTag = false//是否展示标签 tag 一般主页才用的到

    constructor(data: ArrayList<AriticleResponse>?, showTag: Boolean) : this(data) {
        this.showTag = showTag
    }

    init {
        //初始化
        multiTypeDelegate = object : MultiTypeDelegate<AriticleResponse>() {
            override fun getItemType(entity: AriticleResponse): Int {
                //根据是否有图片 判断为文章还是项目，好像有点low的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                return if (TextUtils.isEmpty(entity.envelopePic)) Ariticle else Project
            }
        }
        //注册多布局
        multiTypeDelegate
                .registerItemType(Ariticle, R.layout.item_ariticle)
                .registerItemType(Project, R.layout.item_project)
    }

    override fun convert(helper: BaseViewHolder, item: AriticleResponse?) {
        if (item != null) {
            when (helper.itemViewType) {
                Ariticle -> {
                    //文章布局的赋值
                    item.run{
                        helper.setText(R.id.item_home_author, author)
                        helper.setText(R.id.item_home_content, Html.fromHtml(title))
                        helper.setText(R.id.item_home_type2, "$superChapterName·$chapterName")
                        helper.setText(R.id.item_home_date, niceDate)
                        helper.getView<CollectView>(R.id.item_home_collect).isChecked = collect
                        if (showTag) {
                            //展示标签
                            helper.setGone(R.id.item_home_new, fresh)
                            helper.setGone(R.id.item_home_top, type == 1)
                            if (tags.isNotEmpty()) {
                                helper.setGone(R.id.item_home_type1, true)
                                helper.setText(R.id.item_home_type1, tags[0].name)
                            } else {
                                helper.setGone(R.id.item_home_type1, false)
                            }
                        } else {
                            //隐藏所有标签
                            helper.setGone(R.id.item_home_top, false)
                            helper.setGone(R.id.item_home_type1, false)
                            helper.setGone(R.id.item_home_new, false)
                        }
                    }
                    helper.getView<CollectView>(R.id.item_home_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                        }
                    })
                }
                Project -> {
                    //项目布局的赋值
                    item.run{
                        helper.setText(R.id.item_project_author, author)
                        helper.setText(R.id.item_project_title, Html.fromHtml(title))
                        helper.setText(R.id.item_project_content, Html.fromHtml(desc))
                        helper.setText(R.id.item_project_type, "$superChapterName·$chapterName")
                        helper.setText(R.id.item_project_date, niceDate)
                        if (showTag) {
                            //展示标签
                            helper.setGone(R.id.item_project_new, fresh)
                            helper.setGone(R.id.item_project_top, type == 1)
                            if (tags.isNotEmpty()) {
                                helper.setGone(R.id.item_project_type1, true)
                                helper.setText(R.id.item_project_type1, tags[0].name)
                            } else {
                                helper.setGone(R.id.item_project_type1, false)
                            }
                        } else {
                            //隐藏所有标签
                            helper.setGone(R.id.item_project_top, false)
                            helper.setGone(R.id.item_project_type1, false)
                            helper.setGone(R.id.item_project_new, false)
                        }
                        helper.getView<CollectView>(R.id.item_project_collect).isChecked = collect
                        ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader().loadImage(mContext.applicationContext,
                                ImageConfigImpl
                                        .builder()
                                        .url(envelopePic)
                                        .imageView(helper.getView(R.id.item_project_imageview))
                                        .errorPic(R.drawable.default_project_img)
                                        .fallback(R.drawable.default_project_img)
                                        .isCrossFade(true)
                                        .build()
                        )
                    }
                    helper.getView<CollectView>(R.id.item_project_collect).setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            mOnCollectViewClickListener?.onClick(helper, v, helper.adapterPosition)
                        }
                    })
                }
            }
        }


    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }


    interface OnCollectViewClickListener {
        fun onClick(helper: BaseViewHolder, v: CollectView, position: Int)
    }
}


