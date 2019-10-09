package me.hegj.wandroid.di.component.share

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.share.ShareListModule

import com.jess.arms.di.scope.ActivityScope
import me.hegj.wandroid.mvp.ui.activity.share.ShareListActivity


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/08/2019 13:26
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = arrayOf(ShareListModule::class), dependencies = arrayOf(AppComponent::class))
interface ShareListComponent {
    fun inject(activity: ShareListActivity)
}
