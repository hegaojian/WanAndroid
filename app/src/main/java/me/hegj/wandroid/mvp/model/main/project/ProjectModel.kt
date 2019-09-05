package me.hegj.wandroid.mvp.model.main.project

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.FragmentScope
import io.reactivex.Observable
import javax.inject.Inject

import me.hegj.wandroid.mvp.contract.main.project.ProjectContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.ClassifyResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 13:58
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class ProjectModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), ProjectContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

   /* override fun getTitles(): Observable<ApiResponse<MutableList<ClassifyResponse>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getProjecTypes())
                .flatMap { apiResponseObservable ->
                    mRepositoryManager.obtainCacheService(TitleCache::class.java)
                            .getTitles(apiResponseObservable, DynamicKey("project"), EvictProvider(true))
                            .map {
                                it.data
                            }
                }
    }*/

    override fun getTitles(): Observable<ApiResponse<MutableList<ClassifyResponse>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getProjecTypes())
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
