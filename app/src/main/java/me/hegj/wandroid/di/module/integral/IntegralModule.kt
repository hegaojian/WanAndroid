package me.hegj.wandroid.di.module.integral

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.integral.IntegralContract
import me.hegj.wandroid.mvp.model.integral.IntegralModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/01/2019 08:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建IntegralModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class IntegralModule(private val view: IntegralContract.View) {
    @ActivityScope
    @Provides
    fun provideIntegralView(): IntegralContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideIntegralModel(model: IntegralModel): IntegralContract.Model {
        return model
    }
}
