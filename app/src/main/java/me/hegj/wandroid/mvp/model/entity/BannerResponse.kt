package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable
/**
 * 轮播图
 */
data class BannerResponse(
        var desc: String,
        var id: Int,
        var imagePath: String,
        var isVisible: Int,
        var order: Int,
        var title: String,
        var type: Int,
        var url: String):Serializable


