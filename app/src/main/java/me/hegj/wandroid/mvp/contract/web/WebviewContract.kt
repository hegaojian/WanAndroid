package me.hegj.wandroid.mvp.contract.web

import com.jess.arms.mvp.IView
import com.jess.arms.mvp.IModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.CollectUrlResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/10/2019 09:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
interface WebviewContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView{
        fun  collect(collected:Boolean)
        fun  collectUrlSucc(collected:Boolean,data:CollectUrlResponse)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel{
        fun collect(id:Int): Observable<ApiResponse<Any>>
        fun collectUrl(name:String,link:String): Observable<ApiResponse<CollectUrlResponse>>
        fun uncollect(id:Int): Observable<ApiResponse<Any>>
        fun uncollectList(id:Int,originId:Int): Observable<ApiResponse<Any>>
        fun uncollectUrl(id:Int): Observable<ApiResponse<Any>>
    }

}
