package me.hegj.wandroid.di.module.main.publicNumber

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.publicNumber.PublicChildContract
import me.hegj.wandroid.mvp.model.main.publicNumber.PublicChildModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/09/2019 11:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建PublicChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class PublicChildModule(private val view: PublicChildContract.View) {
    @FragmentScope
    @Provides
    fun providePublicChildView(): PublicChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun providePublicChildModel(model: PublicChildModel): PublicChildContract.Model {
        return model
    }
}
