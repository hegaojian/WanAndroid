package me.hegj.wandroid.di.component.start

import com.jess.arms.di.component.AppComponent
import com.jess.arms.di.scope.ActivityScope
import dagger.Component
import me.hegj.wandroid.di.module.start.LoginModule
import me.hegj.wandroid.mvp.ui.activity.start.LoginActivity
import me.hegj.wandroid.mvp.ui.activity.start.RegisterActivity


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/05/2019 16:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = arrayOf(LoginModule::class), dependencies = arrayOf(AppComponent::class))
interface LoginComponent {
    fun inject(activity: LoginActivity)
    fun inject1(activity: RegisterActivity)
}
