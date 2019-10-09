package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * 分享人信息
 */
data class CoinInfo(var coinCount: Int, var rank:Int, var userId: Int,var username:String) : Serializable
