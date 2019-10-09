package me.hegj.wandroid.di.module.share

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.share.ShareByIdContract
import me.hegj.wandroid.mvp.model.share.ShareByIdModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/09/2019 13:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建ShareByIdModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ShareByIdModule(private val view: ShareByIdContract.View) {
    @ActivityScope
    @Provides
    fun provideShareByIdView(): ShareByIdContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideShareByIdModel(model: ShareByIdModel): ShareByIdContract.Model {
        return model
    }
}
