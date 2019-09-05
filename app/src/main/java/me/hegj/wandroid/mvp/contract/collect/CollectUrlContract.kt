package me.hegj.wandroid.mvp.contract.collect

import com.jess.arms.mvp.IView
import com.jess.arms.mvp.IModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.CollectUrlResponse


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/31/2019 11:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
interface CollectUrlContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView{
        fun requestDataUrlSucc(apiPagerResponse: MutableList<CollectUrlResponse>)
        fun requestDataFaild(errorMsg: String)
        fun uncollect(position:Int)
        fun uncollectFaild(position:Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel{
        fun getCollectUrlDatas(): Observable<ApiResponse<MutableList<CollectUrlResponse>>>
        fun uncollectList(id:Int): Observable<ApiResponse<Any>>
    }

}
