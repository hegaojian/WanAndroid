package me.hegj.wandroid.app.utils

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tencent.mmkv.MMKV
import me.hegj.wandroid.mvp.model.entity.*

object CacheUtil {

    /**
     * 获取保存的账户信息
     */
    fun getUser(): UserInfoResponse {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            UserInfoResponse(false, listOf(), mutableListOf(), "", "", "", "", "", "", 0, "")
        } else {
            Gson().fromJson(userStr, UserInfoResponse::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: UserInfoResponse?) {
        val kv = MMKV.mmkvWithID("app")
        if(userResponse==null){
            kv.encode("user","")
            setCookie("")
            setIsLogin(false)
        }else{
            kv.encode("user",Gson().toJson(userResponse))
            setIsLogin(true)
        }

    }

    /**
     * 是否已经登录
     */
    fun isLogin():Boolean{
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("login",false)
    }

    /**
     * 设置是否已经登录
     */
    fun setIsLogin(isLogin:Boolean){
        val kv = MMKV.mmkvWithID("app")
        kv.encode("login",isLogin)
    }

    /**
     * 获取Cookie信息
     */
    fun  getCookie(): String?{
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString("cookie")
    }

    /**
     * 保存Cookie信息
     */
    fun setCookie(cookie:String){
        val kv = MMKV.mmkvWithID("app")
        kv.encode("cookie",cookie)
    }

    /**
     * 获取项目分类中的标题集合缓存
     */
    fun getProjectTitles(): MutableList<ClassifyResponse> {
        val kv = MMKV.mmkvWithID("cache")
        val projCacheStr =  kv.decodeString("proj")
        if (!TextUtils.isEmpty(projCacheStr)) {
            return Gson().fromJson<MutableList<ClassifyResponse>>(projCacheStr
                    , object : TypeToken<MutableList<ClassifyResponse>>() {}.type)
        }
        return mutableListOf()
    }

    fun setProjectTitles(projectTitleStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("proj",projectTitleStr)
    }

    /**
     * 获取公众号分类中的标题集合缓存
     */
    fun getPublicTitles(): MutableList<ClassifyResponse> {
        val kv = MMKV.mmkvWithID("cache")
        val publicCacheStr =  kv.decodeString("public")
        if (!TextUtils.isEmpty(publicCacheStr)) {
            return Gson().fromJson<MutableList<ClassifyResponse>>(publicCacheStr
                    , object : TypeToken<MutableList<ClassifyResponse>>() {}.type)
        }
        return mutableListOf()
    }

    fun setPublicTitles(publicTitlesStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("public",publicTitlesStr)
    }

    /**
     * 获取热门搜索缓存数据
     */
    fun getSearchData(): MutableList<SearchResponse> {
        val kv = MMKV.mmkvWithID("cache")
        val searchCacheStr =  kv.decodeString("search")
        if (!TextUtils.isEmpty(searchCacheStr)) {
            return Gson().fromJson<MutableList<SearchResponse>>(searchCacheStr
                    , object : TypeToken<MutableList<SearchResponse>>() {}.type)
        }
        return mutableListOf()
    }

    fun setSearchData(searchResponseStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("search",searchResponseStr)
    }

    /**
     * 获取搜索历史缓存数据
     */
    fun getSearchHistoryData(): MutableList<String> {
        val kv = MMKV.mmkvWithID("cache")
        val searchCacheStr =  kv.decodeString("history")
        if (!TextUtils.isEmpty(searchCacheStr)) {
            return Gson().fromJson<MutableList<String>>(searchCacheStr
                    , object : TypeToken<MutableList<String>>() {}.type)
        }
        return mutableListOf()
    }

    fun setSearchHistoryData(searchResponseStr: String) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("history",searchResponseStr)
    }

    /**
     * 获取体系缓存数据
     */
    fun getSystemHistoryData(): MutableList<SystemResponse> {
        val kv = MMKV.mmkvWithID("cache")
        val searchCacheStr =  kv.decodeString("system")
        if (!TextUtils.isEmpty(searchCacheStr)) {
            return Gson().fromJson<MutableList<SystemResponse>>(searchCacheStr
                    , object : TypeToken<MutableList<SystemResponse>>() {}.type)
        }
        return mutableListOf()
    }

    fun setSystemHistoryData(systemResponseStr: MutableList<SystemResponse>) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("system",Gson().toJson(systemResponseStr))
    }

    /**
     * 获取导航缓存数据
     */
    fun getNavigationHistoryData(): MutableList<NavigationResponse> {
        val kv = MMKV.mmkvWithID("cache")
        val searchCacheStr =  kv.decodeString("navigation")
        if (!TextUtils.isEmpty(searchCacheStr)) {
            return Gson().fromJson<MutableList<NavigationResponse>>(searchCacheStr
                    , object : TypeToken<MutableList<NavigationResponse>>() {}.type)
        }
        return mutableListOf()
    }

    fun setNavigationHistoryData(systemResponseStr: MutableList<NavigationResponse>) {
        val kv = MMKV.mmkvWithID("cache")
        kv.encode("navigation",Gson().toJson(systemResponseStr))
    }

}