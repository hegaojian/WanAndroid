package me.hegj.wandroid.mvp.presenter.integral

import android.app.Application

import com.jess.arms.integration.AppManager
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hegj.wandroid.app.utils.HttpUtils
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

import me.hegj.wandroid.mvp.contract.integral.IntegralContract
import me.hegj.wandroid.mvp.model.entity.*
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay

@ActivityScope
class IntegralPresenter
@Inject
constructor(model: IntegralContract.Model, rootView: IntegralContract.View) :
        BasePresenter<IntegralContract.Model, IntegralContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager


    /**
     * 获取积分排行数据
     */
    fun getIntegralData(pageNo: Int) {
        mModel.getIntegralData(pageNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<IntegralResponse>>>) {
                        if (response.isSucces()) {
                            mRootView.requestDataSucces(response.data)
                        } else {
                            mRootView.requestDataFaild(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestDataFaild(HttpUtils.getErrorText(t))
                    }
                })
    }

    /**
     * 获取积分历史数据
     */
    fun getIntegralHistoryData(pageNo: Int) {
        mModel.getIntegralHistoryData(pageNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<IntegralHistoryResponse>>>) {
                        if (response.isSucces()) {
                            mRootView.requestHistoryDataSucces(response.data)
                        } else {
                            mRootView.requestDataFaild(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestDataFaild(HttpUtils.getErrorText(t))
                    }
                })
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}
