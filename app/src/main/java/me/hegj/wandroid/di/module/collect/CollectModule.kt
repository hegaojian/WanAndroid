package me.hegj.wandroid.di.module.collect

import com.jess.arms.di.scope.ActivityScope
import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.collect.CollectContract
import me.hegj.wandroid.mvp.model.collect.CollectModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/29/2019 11:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建CollectModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class CollectModule(private val view: CollectContract.View) {
    @FragmentScope
    @Provides
    fun provideCollectView(): CollectContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideCollectModel(model: CollectModel): CollectContract.Model {
        return model
    }
}
