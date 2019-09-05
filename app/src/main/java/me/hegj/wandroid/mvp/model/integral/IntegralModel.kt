package me.hegj.wandroid.mvp.model.integral

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.contract.integral.IntegralContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.IntegralHistoryResponse
import me.hegj.wandroid.mvp.model.entity.IntegralResponse
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/01/2019 08:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class IntegralModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), IntegralContract.Model {

    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getIntegralData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getIntegralRank(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
    override fun getIntegralHistoryData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getIntegralHistory(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }
}
