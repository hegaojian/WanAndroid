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
import me.hegj.wandroid.mvp.contract.todo.TodoContract
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.TodoResponse
import me.jessyan.rxerrorhandler.core.RxErrorHandler
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber
import me.jessyan.rxerrorhandler.handler.RetryWithDelay
import javax.inject.Inject

@ActivityScope
class TodoPresenter
@Inject
constructor(model: TodoContract.Model, rootView: TodoContract.View) :
        BasePresenter<TodoContract.Model, TodoContract.View>(model, rootView) {
    @Inject
    lateinit var mErrorHandler: RxErrorHandler
    @Inject
    lateinit var mApplication: Application
    @Inject
    lateinit var mImageLoader: ImageLoader
    @Inject
    lateinit var mAppManager: AppManager

    /**
     * 获取待办任务数据
     */
    fun getTodoData(pageNo: Int) {
        mModel.getTodoData(pageNo)
                .subscribeOn(Schedulers.io())
                .retryWhen(RetryWithDelay(1, 0))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(object : ErrorHandleSubscriber<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>(mErrorHandler) {
                    override fun onNext(response: ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>) {
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


    fun delTodo(id: Int, position: Int) {
        mModel.deleteTodoData(id)
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
                            mRootView.deleteTodoDataSucc(position)
                        } else {
                            mRootView.updateTodoDataFaild(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.updateTodoDataFaild(HttpUtils.getErrorText(t))
                    }
                })
    }

    fun updateTodo(id: Int, position: Int) {
        mModel.updateTodoData(id, 1)//1 完成
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
                            mRootView.updateTodoDataSucc(position)
                        } else {
                            mRootView.updateTodoDataFaild(response.errorMsg)
                        }
                    }

                    override fun onError(t: Throwable) {
                        super.onError(t)
                        mRootView.updateTodoDataFaild(HttpUtils.getErrorText(t))
                    }
                })
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
