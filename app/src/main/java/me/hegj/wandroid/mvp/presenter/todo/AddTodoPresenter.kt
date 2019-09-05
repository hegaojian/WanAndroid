package me.hegj.wandroid.mvp.presenter.todo

import android.app.Application
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.http.imageloader.ImageLoader
import com.jess.arms.integration.AppManager
import com.jess.arms.mvp.BasePresenter
import com.jess.arms.utils.RxLifecycleUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import me.hegj.wandroid.app.utils.HttpUtils
import me.hegj.wandroid.mvp.contract.todo.AddTodoContract
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/03/2019 21:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class AddTodoPresenter
@Inject
constructor(model: AddTodoContract.Model, rootView: AddTodoContract.View) :
        BasePresenter<AddTodoContract.Model, AddTodoContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager


    fun addTodo(title: String, content: String, date: String, priority: Int) {
        mModel.addTodo(title, content, date, 0, priority)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe {
                    mRootView.showLoading()//显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mRootView.hideLoading()//隐藏加载框
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            mRootView.addTodoSucc()
                        } else {
                            mRootView.addTodoFaild(response.errorMsg)
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.addTodoFaild(HttpUtils.getErrorText(t))
                    }
                })
    }

    fun updateTodo(title: String, content: String, date: String, priority: Int,id: Int) {
        mModel.updateTodo(title, content, date, 0, priority,id)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe {
                    mRootView.showLoading()//显示加载框
                }
                .subscribeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    mRootView.hideLoading()//隐藏加载框
                }
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<Any>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<Any>) {
                        if (response.isSucces()) {
                            mRootView.addTodoSucc()
                        } else {
                            mRootView.addTodoFaild(response.errorMsg)
                        }
                    }
                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.addTodoFaild(HttpUtils.getErrorText(t))
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
