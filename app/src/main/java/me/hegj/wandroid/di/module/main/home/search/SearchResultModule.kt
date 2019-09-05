package me.hegj.wandroid.di.module.main.home.search

import com.jess.arms.di.scope.ActivityScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.home.search.SearchResultContract
import me.hegj.wandroid.mvp.model.main.home.search.SearchResultModel


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
@Module
//构建SearchResultModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class SearchResultModule(private val view: SearchResultContract.View) {
    @ActivityScope
    @Provides
    fun provideSearchResultView(): SearchResultContract.View {
        return this.view
    }

    @ActivityScope
    @Provides
    fun provideSearchResultModel(model: SearchResultModel): SearchResultContract.Model {
        return model
    }
}
