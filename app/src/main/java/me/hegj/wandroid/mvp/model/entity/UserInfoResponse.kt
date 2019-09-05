package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable
/**
 * 账户信息
 */
data class UserInfoResponse(var admin: Boolean,
                            var chapterTops: List<String>,
                            var collectIds: MutableList<String>,
                            var email: String,
                            var icon: String,
                            var id: String,
                            var nickname: String,
                            var password: String,
                            var token: String,
                            var type: Int,
                            var username: String) : Serializable
