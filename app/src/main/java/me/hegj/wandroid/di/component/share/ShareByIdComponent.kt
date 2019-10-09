package me.hegj.wandroid.di.component.share

import dagger.Component
import com.jess.arms.di.component.AppComponent

import me.hegj.wandroid.di.module.share.ShareByIdModule

import com.jess.arms.di.scope.ActivityScope
import me.hegj.wandroid.mvp.ui.activity.share.ShareByIdActivity


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 10/09/2019 13:20
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@ActivityScope
@Component(modules = arrayOf(ShareByIdModule::class), dependencies = arrayOf(AppComponent::class))
interface ShareByIdComponent {
    fun inject(activity: ShareByIdActivity)
}
