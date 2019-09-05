package me.hegj.wandroid.di.component.collect

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.collect.CollectUrlModule

import com.jess.arms.di.scope.FragmentScope
import me.hegj.wandroid.mvp.ui.activity.collect.CollectUrlFragment


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/31/2019 11:27
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@FragmentScope
@Component(modules = arrayOf(CollectUrlModule::class), dependencies = arrayOf(AppComponent::class))
interface CollectUrlComponent {
    fun inject(fragment: CollectUrlFragment)
}
