package me.hegj.wandroid.di.component.web

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.web.WebviewModule

import com.jess.arms.di.scope.ActivityScope
import me.hegj.wandroid.mvp.ui.activity.web.WebviewActivity


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/10/2019 09:51
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = arrayOf(WebviewModule::class), dependencies = arrayOf(AppComponent::class))
interface WebviewComponent {
    fun inject(activity: WebviewActivity)
}
