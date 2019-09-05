/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.hegj.wandroid.mvp.model.api

import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.*
import retrofit2.http.*


/**
 * 存放一些与 API 有关的东西,如请求地址,请求码等
 * @Author:         hegaojian
 * @CreateDate:     2019/8/4 15:28
 */
interface Api {

    companion object {
        const val APP_DOMAIN = "https://www.wanandroid.com"
    }

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    fun login(@Field("username") username: String, @Field("password") pwd: String): Observable<ApiResponse<UserInfoResponse>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("/user/register")
    fun register(@Field("username") username: String, @Field("password") pwd: String, @Field("repassword") rpwd: String): Observable<ApiResponse<Any>>


    /**
     * 获取banner数据
     */
    @GET("/banner/json")
    fun getBanner(): Observable<ApiResponse<MutableList<BannerResponse>>>

    /**
     * 获取置顶文章集合数据
     */
    @GET("/article/top/json")
    fun getTopAritrilList(): Observable<ApiResponse<MutableList<AriticleResponse>>>

    /**
     * 获取首页文章数据
     */
    @GET("/article/list/{page}/json")
    fun getAritrilList(@Path("page") pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>

    /**
     * 收藏
     */
    @POST("/lg/collect/{id}/json")
    fun collect(@Path("id") id: Int): Observable<ApiResponse<Any>>

    /**
     * 取消收藏
     */
    @POST("/lg/uncollect_originId/{id}/json")
    fun uncollect(@Path("id") id: Int): Observable<ApiResponse<Any>>


    /**
     * 项目分类
     */
    @GET("/project/tree/json")
    fun getProjecTypes(): Observable<ApiResponse<MutableList<ClassifyResponse>>>

    /**
     * 根据分类id获取项目数据
     */
    @GET("/project/list/{page}/json")
    fun getProjecDataByType(@Path("page") pageNo: Int, @Query("cid") cid: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>

    /**
     * 获取最新项目数据
     */
    @GET("/article/listproject/{page}/json")
    fun getProjecNewData(@Path("page") pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>

    /**
     * 公众号分类
     */
    @GET("/wxarticle/chapters/json")
    fun getPublicTypes(): Observable<ApiResponse<MutableList<ClassifyResponse>>>

    /**
     * 获取公众号数据
     */
    @GET("/wxarticle/list/{id}/{page}/json")
    fun getPublicNewData(@Path("page") pageNo: Int, @Path("id") id: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>


    /**
     * 获取热门搜索数据
     */
    @GET("/hotkey/json")
    fun getSearchData(): Observable<ApiResponse<MutableList<SearchResponse>>>

    /**
     * 根据关键词搜索数据
     */
    @POST("/article/query/{page}/json")
    fun getSearchDataByKey(@Path("page") pageNo: Int, @Query("k") searchKey: String): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>

    /**
     * 获取体系数据
     */
    @GET("/tree/json")
    fun getSystemData(): Observable<ApiResponse<MutableList<SystemResponse>>>

    /**
     * 知识体系下的文章数据
     */
    @GET("/article/list/{page}/json")
    fun getAritrilDataByTree(@Path("page") pageNo: Int, @Query("cid") cid: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>

    /**
     * 获取导航数据
     */
    @GET("/navi/json")
    fun getNavigationData(): Observable<ApiResponse<MutableList<NavigationResponse>>>

    /**
     * 获取收藏文章数据
     */
    @GET("/lg/collect/list/{page}/json")
    fun getCollectData(@Path("page") pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<CollectResponse>>>>

    /**
     * 获取收藏网址数据
     */
    @GET("/lg/collect/usertools/json")
    fun getCollectUrlData(): Observable<ApiResponse<MutableList<CollectUrlResponse>>>

    /**
     * 收藏网址
     */
    @POST("/lg/collect/addtool/json")
    fun collectUrl(@Query("name") name: String, @Query("link") link: String): Observable<ApiResponse<CollectUrlResponse>>

    /**
     * 取消收藏网址
     */
    @POST("/lg/collect/deletetool/json")
    fun deletetool(@Query("id") id: Int): Observable<ApiResponse<Any>>

    /**
     * 取消收藏
     */
    @POST("/lg/uncollect/{id}/json")
    fun uncollectList(@Path("id") id: Int, @Query("originId") originId: Int): Observable<ApiResponse<Any>>

    /**
     * 获取当前账户的个人积分
     */
    @GET("/lg/coin/userinfo/json")
    fun getIntegral(): Observable<ApiResponse<IntegralResponse>>

    /**
     * 获取积分排行榜
     */
    @GET("/coin/rank/{page}/json")
    fun getIntegralRank(@Path("page") page: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>>

    /**
     * 获取积分历史
     */
    @GET("/lg/coin/list/{page}/json")
    fun getIntegralHistory(@Path("page") page: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>>

    /**
     * 获取Todo列表数据 根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    fun getTodoData(@Path("page") page: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(@Field("title") title: String,
                @Field("content") content: String,
                @Field("date") date: String,
                @Field("type") type: Int,
                @Field("priority") priority: Int): Observable<ApiResponse<Any>>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(@Field("title") title: String,
                   @Field("content") content: String,
                   @Field("date") date: String,
                   @Field("type") type: Int,
                   @Field("priority") priority: Int,
                   @Path("id") id: Int): Observable<ApiResponse<Any>>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    fun deleteTodo(@Path("id") id: Int): Observable<ApiResponse<Any>>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): Observable<ApiResponse<Any>>


}
