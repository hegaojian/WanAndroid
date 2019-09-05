package me.hegj.wandroid.mvp.model.main.project

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.contract.main.project.ProjectChildContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.AriticleResponse
import javax.inject.Inject


@FragmentScope
class ProjectChildModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), ProjectChildContract.Model {


    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getProjects(pageNo: Int,cid:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getProjecDataByType(pageNo,cid))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun getNewProjects(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getProjecNewData(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    //取消收藏
    override fun uncollect(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .uncollect(id))
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

}
