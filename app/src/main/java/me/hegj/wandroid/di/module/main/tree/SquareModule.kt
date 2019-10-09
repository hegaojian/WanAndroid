package me.hegj.wandroid.di.module.main.tree

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.tree.SquareContract
import me.hegj.wandroid.mvp.model.main.tree.SquareModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/08/2019 09:49
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建SquareModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SquareModule(private val view: SquareContract.View) {
    @FragmentScope
    @Provides
    fun provideSquareView(): SquareContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideSquareModel(model: SquareModel): SquareContract.Model {
        return model
    }
}
