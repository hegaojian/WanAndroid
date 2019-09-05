package me.hegj.wandroid.di.module.main.publicNumber

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.publicNumber.PublicContract
import me.hegj.wandroid.mvp.model.main.publicNumber.PublicModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 14:02
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建PublicModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class PublicModule(private val view: PublicContract.View) {
    @FragmentScope
    @Provides
    fun providePublicView(): PublicContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun providePublicModel(model: PublicModel): PublicContract.Model {
        return model
    }
}
