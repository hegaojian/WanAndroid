package me.hegj.wandroid.di.component.main.tree

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.main.tree.NavigationModule

import com.jess.arms.di.scope.FragmentScope
import me.hegj.wandroid.mvp.ui.activity.main.tree.NavigationFragment


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/14/2019 11:40
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = arrayOf(NavigationModule::class), dependencies = arrayOf(AppComponent::class))
interface NavigationComponent {
    fun inject(fragment: NavigationFragment)
}
