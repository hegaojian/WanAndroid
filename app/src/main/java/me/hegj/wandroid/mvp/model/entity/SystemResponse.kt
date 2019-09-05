package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable

/**
 *  体系数据
  * @Author:         hegaojian
  * @CreateDate:     2019/8/21 15:46
 */
data class SystemResponse(var children: MutableList<ClassifyResponse>,
                          var courseId: Int,
                          var id: Int,
                          var name: String,
                          var order: Int,
                          var parentChapterId: Int,
                          var userControlSetTop: Boolean,
                          var visible: Int) : Serializable
