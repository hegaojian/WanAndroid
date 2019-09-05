package me.hegj.wandroid.di.component

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.MainModule

import com.jess.arms.di.scope.FragmentScope
import me.hegj.wandroid.mvp.ui.activity.main.MainFragment


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/08/2019 10:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = arrayOf(MainModule::class), dependencies = arrayOf(AppComponent::class))
interface MainComponent {
    fun inject(fragment: MainFragment)
}
