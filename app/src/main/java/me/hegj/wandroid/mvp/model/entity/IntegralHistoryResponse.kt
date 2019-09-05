package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * 积分记录
 */
data class IntegralHistoryResponse(
        var coinCount: Int,
        var date: Long,
        var desc: String,
        var id: Int,
        var type: Int,
        var reason: String,
        var userId: Int,
        var userName: String) : Serializable


