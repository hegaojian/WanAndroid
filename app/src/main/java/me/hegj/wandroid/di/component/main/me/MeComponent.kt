package me.hegj.wandroid.di.component.main.me

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.main.me.MeModule

import com.jess.arms.di.scope.FragmentScope
import me.hegj.wandroid.mvp.ui.activity.main.me.MeFragment


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 07/31/2019 14:03
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = arrayOf(MeModule::class), dependencies = arrayOf(AppComponent::class))
interface MeComponent {
    fun inject(fragment: MeFragment)
}
