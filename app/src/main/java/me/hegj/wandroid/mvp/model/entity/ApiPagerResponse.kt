package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable
/**
 * 一般有列表的基类
 */
data class ApiPagerResponse<T>(var datas: T,
                               var curPage:Int,
                               var offset:Int,
                               var over:Boolean,
                               var pageCount:Int,
                               var size:Int,
                               var total:Int): Serializable