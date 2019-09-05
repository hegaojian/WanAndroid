package me.hegj.wandroid.di.module.todo

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.todo.TodoContract
import me.hegj.wandroid.mvp.model.todo.TodoModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 09/01/2019 13:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建TodoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TodoModule(private val view: TodoContract.View) {
    @ActivityScope
    @Provides
    fun provideTodoView(): TodoContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideTodoModel(model: TodoModel): TodoContract.Model {
        return model
    }
}
