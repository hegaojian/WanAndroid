package me.hegj.wandroid.mvp.presenter.main.home.search

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hegj.wandroid.app.utils.CacheUtil
import me.hegj.wandroid.mvp.contract.main.home.search.SearchContract
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.SearchResponse
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/16/2019 20:31
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class SearchPresenter
@Inject
constructor(model: SearchContract.Model, rootView: SearchContract.View) :
        BasePresenter<SearchContract.Model, SearchContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    fun  getHotData(){
        mModel.getHotData()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<SearchResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<SearchResponse>>) {
                        if (response.isSucces()) {
                            CacheUtil.setSearchData( Gson().toJson(response.data))
                            mRootView.requestSearchSucc(response.data)
                        }else{
                            mRootView.requestSearchSucc(CacheUtil.getSearchData())
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestSearchSucc(CacheUtil.getSearchData())
                    }
                })

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
