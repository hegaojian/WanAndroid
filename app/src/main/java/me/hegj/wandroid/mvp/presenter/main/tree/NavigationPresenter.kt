package me.hegj.wandroid.mvp.presenter.main.tree

import android.app.Application

import com.jess.arms.integration.AppManager
import com.jess.arms.di.scope.FragmentScope
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.utils.RxLifecycleUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hegj.wandroid.app.utils.CacheUtil
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import javax.inject.Inject

import me.hegj.wandroid.mvp.contract.main.tree.NavigationContract
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.NavigationResponse
import me.hegj.wandroid.mvp.model.entity.SystemResponse
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/14/2019 11:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
class NavigationPresenter
@Inject
constructor(model: NavigationContract.Model, rootView: NavigationContract.View) :
        BasePresenter<NavigationContract.Model, NavigationContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager


    fun getNavigationData(){
        mModel.getNavigationData()
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindUntilEvent(mRootView, FragmentEvent.DESTROY))//fragment的绑定方式  使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<MutableList<NavigationResponse>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<MutableList<NavigationResponse>>) {
                        if (response.isSucces()) {
                            //请求成功 保存数据
                            CacheUtil.setNavigationHistoryData(response.data)
                            //回调数据给activity
                            mRootView.getNavigationDataSucc(response.data)
                        } else {
                            //请求失败，回调缓存数据给activity
                            mRootView.getNavigationDataSucc(CacheUtil.getNavigationHistoryData())
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //请求失败，回调缓存数据给activity
                        mRootView.getNavigationDataSucc(CacheUtil.getNavigationHistoryData())
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
