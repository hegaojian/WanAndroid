package me.hegj.wandroid.di.module.main.project

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.project.ProjectChildContract
import me.hegj.wandroid.mvp.model.main.project.ProjectChildModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/07/2019 17:34
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建ProjectChildModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class ProjectChildModule(private val view: ProjectChildContract.View) {
    @FragmentScope
    @Provides
    fun provideProjectChildView(): ProjectChildContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideProjectChildModel(model: ProjectChildModel): ProjectChildContract.Model {
        return model
    }
}
