package me.hegj.wandroid.mvp.contract.main.home

import com.jess.arms.mvp.IView
import com.jess.arms.mvp.IModel
import io.reactivex.Observable
import me.hegj.wandroid.mvp.model.entity.*


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 13:52
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
interface HomeContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View : IView{
        fun  requestBannerSucces(banners : MutableList<BannerResponse>)
        fun  requestAritilSucces(ariticles : ApiPagerResponse<MutableList<AriticleResponse>>)
        fun  requestAritilFaild(errorMsg:String)
        fun  collect(collected:Boolean,position:Int)
    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model : IModel{
        //获取主页文章列表
        fun getArilist(pageNo:Int): Observable<ApiResponse<ApiPagerResponse<MutableList<AriticleResponse>>>>
        fun getTopArilist(): Observable<ApiResponse<MutableList<AriticleResponse>>>
        fun getBannList(): Observable<ApiResponse<MutableList<BannerResponse>>>
        fun collect(id:Int):Observable<ApiResponse<Any>>
        fun uncollect(id:Int):Observable<ApiResponse<Any>>
    }

}
