package me.hegj.wandroid.mvp.presenter.main.home.search

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

import me.hegj.wandroid.mvp.contract.main.home.search.SearchResultContract
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.AriticleResponse
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/19/2019 09:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class SearchResultPresenter
@Inject
constructor(model: SearchResultContract.Model, rootView: SearchResultContract.View) :
        BasePresenter<SearchResultContract.Model, SearchResultContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    /**
     * 获取文章列表集合数据
     */
    fun getAriList(pageNo: Int, searchKey: String) {
        mModel.getArilist(pageNo, searchKey)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>) {
                        if (response.isSucces()) {
                            mRootView.requestAritilSucces(response.data)
                        } else {
                            mRootView.requestAritilFaild(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.requestAritilFaild(HttpUtils.getErrorText(t))
                    }
                })
    }

    /**
     * 收藏
     */
    fun collect(id: Int, position: Int) {
        mModel.collect(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            //收藏成功
                            mRootView.collect(true, position)
                        } else {
                            //收藏失败
                            mRootView.collect(false, position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //收藏失败
                        mRootView.collect(false, position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })
    }

    /**
     * 取消收藏
     */
    fun uncollect(id: Int, position: Int) {
        mModel.uncollect(id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            //取消收藏成功
                            mRootView.collect(false, position)
                        } else {
                            //取消收藏失败
                            mRootView.collect(true, position)
                            mRootView.showMessage(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        //取消收藏失败
                        mRootView.collect(true, position)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy();
    }
}
