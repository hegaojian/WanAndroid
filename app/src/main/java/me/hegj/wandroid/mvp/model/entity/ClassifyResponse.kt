package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable

/**
 * 项目分类
 */
data class ClassifyResponse(var children: List<Any>,
                            var courseId: Int,
                            var id: Int,
                            var name: String,
                            var order: Int,
                            var parentChapterId: Int,
                            var userControlSetTop: Boolean,
                            var visible: Int) : Serializable
