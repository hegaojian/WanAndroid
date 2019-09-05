package me.hegj.wandroid.di.module.main.tree.treeinfo

import com.jess.arms.di.scope.FragmentScope

import dagger.Module
import dagger.Provides

import me.hegj.wandroid.mvp.contract.main.tree.treeinfo.TreeinfoContract
import me.hegj.wandroid.mvp.model.main.tree.treeinfo.TreeinfoModel


/**
 * ================================================
 * Description:
 * <p>
 * Created by MVPArmsTemplate on 08/23/2019 17:12
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms">Star me</a>
 * <a href="https://github.com/JessYanCoding/MVPArms/wiki">See me</a>
 * <a href="https://github.com/JessYanCoding/MVPArmsTemplate">模版请保持更新</a>
 * ================================================
 */
@Module
//构建TreeinfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
class TreeinfoModule(private val view: TreeinfoContract.View) {
    @FragmentScope
    @Provides
    fun provideTreeinfoView(): TreeinfoContract.View {
        return this.view
    }

    @FragmentScope
    @Provides
    fun provideTreeinfoModel(model: TreeinfoModel): TreeinfoContract.Model {
        return model
    }
}
