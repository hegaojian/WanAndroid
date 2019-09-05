package me.hegj.wandroid.mvp.model.entity

import java.io.Serializable
/**
 * 基类
 */
open class ApiResponse<T>(var data: T, var errorCode: Int, var errorMsg: String) : Serializable {

    fun isSucces(): Boolean {
        return errorCode == 0
    }
}