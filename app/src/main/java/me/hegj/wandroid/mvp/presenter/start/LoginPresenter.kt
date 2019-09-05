package me.hegj.wandroid.mvp.presenter.start

import android.app.Application
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hegj.wandroid.app.utils.HttpUtils
import me.hegj.wandroid.mvp.contract.start.LoginContract
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.UserInfoResponse
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import java.lang.Exception
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/05/2019 16:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class LoginPresenter
@Inject
constructor(model: LoginContract.Model, rootView: LoginContract.View) :
        BasePresenter<LoginContract.Model, LoginContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mAppManager: AppManager

    fun login(name: String, pwd: String) {
        mModel.login(name, pwd)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔秒数
                .doOnSubscribe {
                    mRootView.showLoading()//显示加载框
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mRootView.hideLoading()//隐藏加载框
                }
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<UserInfoResponse>>(mErrorHandler) {
                    override fun onNext(users: ApiResponse<UserInfoResponse>) {
                        if (users.errorCode != -1) {
                            mRootView.onSucc(users.data)
                        } else {
                            mRootView.showMessage(users.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })
    }

    fun register(name: String, pwd: String, pwd1: String) {
        mModel.register(name, pwd, pwd1)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe {
                    mRootView.showLoading()//显示加载框
                }.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .doFinally {
                    mRootView.hideLoading()//隐藏加载框
                }
                .flatMap {
                    //转换，如果注册成功，直接调起登录，失败则跑出异常
                    if (it.errorCode != -1) {
                        mModel.login(name, pwd)
                    } else {
                        throw Exception(it.errorMsg)
                    }
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<UserInfoResponse>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<UserInfoResponse>) {
                        if (response.errorCode != -1) {
                            mRootView.onSucc(response.data)
                        } else {
                            mRootView.showMessage(response.errorMsg)
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.showMessage(HttpUtils.getErrorText(t))
                    }
                })

    }
}
