package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * 导航数据
  * @Author:         hegaojian
  * @CreateDate:     2019/8/26 17:40
 */
data class NavigationResponse(var articles: MutableList<AriticleResponse>,
                              var cid: Int,
                              var name: String) : Serializable
