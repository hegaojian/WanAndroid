package me.hegj.wandroid.di.component.main.home.search

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.main.home.search.SearchResultModule

import com.jess.arms.di.scope.ActivityScope
import me.hegj.wandroid.mvp.ui.activity.main.home.search.SearchResultActivity


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/19/2019 09:32
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = arrayOf(SearchResultModule::class), dependencies = arrayOf(AppComponent::class))
interface SearchResultComponent {
    fun inject(activity: SearchResultActivity)
}
