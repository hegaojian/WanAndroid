package me.hegj.wandroid.mvp.model.todo

import android.app.Application
import com.google.gson.Gson
import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.integration.IRepositoryManager
import com.jess.arms.mvp.BaseModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.contract.todo.TodoContract
import me.hegj.wandroid.mvp.model.api.Api
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.TodoResponse
import javax.inject.Inject


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/01/2019 13:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
class TodoModel
@Inject
constructor(repositoryManager: IRepositoryManager) : BaseModel(repositoryManager), TodoContract.Model {


    @Inject
    lateinit var mGson: Gson
    @Inject
    lateinit var mApplication: Application

    override fun getTodoData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .getTodoData(pageNo))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun updateTodoData(id: Int, status: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .doneTodo(id, status))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun deleteTodoData(id: Int): Observable<ApiResponse<Any>> {
        return Observable.just(mRepositoryManager.obtainRetrofitService(Api::class.java)
                .deleteTodo(id))
                .flatMap { apiResponseObservable ->
                    apiResponseObservable
                }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
