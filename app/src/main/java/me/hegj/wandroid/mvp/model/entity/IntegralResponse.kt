package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * 积分
 */
data class IntegralResponse(
        var coinCount: Int,//当前积分
        var rank: Int,
        var userId: Int,
        var username: String) : Serializable


