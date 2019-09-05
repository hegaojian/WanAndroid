package me.hegj.wandroid.mvp.contract.todo

import com.jess.arms.mvp.IModel
import com.jess.arms.mvp.IView
import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.TodoResponse

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
interface TodoContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView {
        fun requestDataSucces(ariticles: ApiPagerResponse<MutableList<TodoResponse>>)
        fun requestDataFaild(errorMsg: String)
        fun updateTodoDataSucc(position: Int)
        fun deleteTodoDataSucc(position: Int)
        fun updateTodoDataFaild(errorMsg: String)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel {
        fun getTodoData(pageNo: Int): Observable<ApiResponse<ApiPagerResponse<MutableList<TodoResponse>>>>
        fun updateTodoData(id: Int, status: Int): Observable<ApiResponse<Any>>
        fun deleteTodoData(id: Int): Observable<ApiResponse<Any>>
    }

}
