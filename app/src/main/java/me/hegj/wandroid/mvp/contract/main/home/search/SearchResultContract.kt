package me.hegj.wandroid.mvp.contract.main.home.search

import com.jess.arms.mvp.IView
import com.jess.arms.mvp.IModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.ApiPagerResponse
import me.hegj.wandroid.mvp.model.entity.ApiResponse
import me.hegj.wandroid.mvp.model.entity.AriticleResponse


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
interface SearchResultContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView{
        fun  requestAritilSucces(ariticles : ApiPagerResponse<MutableList<AriticleResponse>>)
        fun  requestAritilFaild(errorMsg:String)
        fun  collect(collected:Boolean,position:Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel{
        fun getArilist(pageNo:Int,searchKey:String): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>
        fun collect(id:Int): Observable<ApiResponse<Any>>
        fun uncollect(id:Int): Observable<ApiResponse<Any>>
    }

}
