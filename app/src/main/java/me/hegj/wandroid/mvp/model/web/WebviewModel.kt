package me.hegj.wandroid.mvp.model.web

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.ActivityScope
import io.reactivex.Observable
import javax.inject.Inject

import me.hegj.wandroid.mvp.contract.web.WebviewContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.CollectUrlResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/10/2019 09:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class WebviewModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), WebviewContract.Model {


    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application


    //取消收藏
    override fun uncollect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .uncollect(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    //取消收藏
    override fun uncollectList(id: Int, originId: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .uncollectList(id,originId))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    //收藏
    override fun collect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collect(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    override fun collectUrl(name: String, link: String): Observable<ApiResponse<CollectUrlResponse>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .collectUrl(name,link))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    //取消收藏网址
    override fun uncollectUrl(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .deletetool(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
