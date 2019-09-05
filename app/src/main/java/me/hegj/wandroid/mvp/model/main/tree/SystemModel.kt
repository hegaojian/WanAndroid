package me.hegj.wandroid.mvp.model.main.tree

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel

import com.jess.arms.di.scope.FragmentScope
import io.reactivex.Observable
import javax.inject.Inject

import me.hegj.wandroid.mvp.contract.main.tree.SystemContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.SystemResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 14:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class SystemModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), SystemContract.Model {
    override fun getSystemData(): Observable<ApiResponse<MutableList<SystemResponse>>> {
        return Observable.just(mRepositoryManager
                .obtainRetrofitService(Api::class.java)
                .getSystemData())
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    @Inject
    lateinit var mGson: Gson;
    @Inject
    lateinit var mApplication: Application;

    override fun onDestroy() {
        super.onDestroy()
    }
}
