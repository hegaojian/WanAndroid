package me.hegj.wandroid.di.module.todo

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.todo.AddTodoContract
import me.hegj.wandroid.mvp.model.todo.AddTodoModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/03/2019 21:45
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建AddTodoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class AddTodoModule(private val view: AddTodoContract.View) {
    @ActivityScope
    @Provides
    fun provideAddTodoView(): AddTodoContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideAddTodoModel(model: AddTodoModel): AddTodoContract.Model {
        return model
    }
}
