package me.hegj.wandroid.di.module.main.tree

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.tree.SystemContract
import me.hegj.wandroid.mvp.model.main.tree.SystemModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 14:01
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建SystemModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SystemModule(private val view: SystemContract.View) {
    @FragmentScope
    @Provides
    fun provideSystemView(): SystemContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideSystemModel(model: SystemModel): SystemContract.Model {
        return model
    }
}
