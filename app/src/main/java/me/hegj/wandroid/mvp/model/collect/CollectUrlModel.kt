package me.hegj.wandroid.mvp.model.collect

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.FragmentScope
import io.reactivex.Observable
import javax.inject.Inject

import me.hegj.wandroid.mvp.contract.collect.CollectUrlContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.CollectUrlResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/31/2019 11:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class CollectUrlModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), CollectUrlContract.Model {
    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getCollectUrlDatas(): Observable<ApiResponse<MutableList<CollectUrlResponse>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getCollectUrlData())
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun uncollectList(id: Int): Observable<ApiResponse<Any>> {
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
