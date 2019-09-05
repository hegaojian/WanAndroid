package me.hegj.wandroid.di.module.main.me

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.me.MeContract
import me.hegj.wandroid.mvp.model.main.me.MeModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 14:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建MeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class MeModule(private val view: MeContract.View) {
    @FragmentScope
    @Provides
    fun provideMeView(): MeContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideMeModel(model: MeModel): MeContract.Model {
        return model
    }
}
