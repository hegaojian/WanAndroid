package me.hegj.wandroid.mvp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.jess.arms.base.BaseFragment
import com.jess.arms.base.delegate.IFragment
import com.jess.arms.integration.cache.Cache
import com.jess.arms.integration.cache.CacheType
import com.jess.arms.integration.lifecycle.FragmentLifecycleable
import com.jess.arms.mvp.IPresenter
import com.jess.arms.mvp.IView
import com.jess.arms.utils.ArmsUtils
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import me.hegj.wandroid.R
import me.hegj.wandroid.app.utils.ShowUtils
import me.yokeyword.fragmentation.SupportFragment
import javax.inject.Inject

/**
 * ================================================
 * 因为 Java 只能单继承, 所以如果要用到需要继承特定 @[Fragment] 的三方库, 那你就需要自己自定义 @[Fragment]
 * 继承于这个特定的 @[Fragment], 然后再按照 [com.jess.arms.base.BaseFragment] 的格式, 将代码复制过去, 记住一定要实现[IFragment]
 *
 * @see [请配合官方 Wiki 文档学习本框架](https://github.com/JessYanCoding/MVPArms/wiki)
 *
 * @see [更新日志, 升级必看!](https://github.com/JessYanCoding/MVPArms/wiki/UpdateLog)
 *
 * @see [常见 Issues, 踩坑必看!](https://github.com/JessYanCoding/MVPArms/wiki/Issues)
 *
 * @see [MVPArms 官方组件化方案 ArmsComponent, 进阶指南!](https://github.com/JessYanCoding/ArmsComponent/wiki)
 * Created by JessYan on 22/03/2016
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
abstract class BaseFragment<P : IPresenter> : SupportFragment(), IFragment, FragmentLifecycleable, IView {
    protected val TAG = this.javaClass.simpleName
    private val mLifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    private var mCache: Cache<*, *>? = null
    protected var mContext: Context? = null
    @Inject
    @JvmField
    var mPresenter: P? = null//如果当前页面逻辑简单, Presenter 可以为 null

    @Synchronized
    override fun provideCache(): Cache<String, Any> {
        if (mCache == null) {
            mCache = ArmsUtils.obtainAppComponentFromContext(activity).cacheFactory().build(CacheType.ACTIVITY_CACHE)
        }
        return mCache as Cache<String, Any>
    }

    override fun provideLifecycleSubject(): Subject<FragmentEvent> {
        return mLifecycleSubject
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return initView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter?.run {
            onDestroy()//释放资源
            null
        }
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    /**
     * 是否使用 EventBus 默认true
     */
    override fun useEventBus(): Boolean {
        return true
    }

    override fun setData(data: Any?) {
        //MVPArms的 fragment通信方法，使用event替代了，这个就不要了
    }

    override fun initData(savedInstanceState: Bundle?) {
        //这是MvpArms的初始化数据方法，fragment只要创建就会执行，没有懒加载效果，我已经用了其他的库替代了，所以这个方法继承后我们可以 实现或不实现都阔以
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun showLoading() {
        ShowUtils.showLoading(_mActivity)
    }

    override fun hideLoading() {
        ShowUtils.dismissLoading()
    }
    override fun showMessage(message: String) {
        ShowUtils.showDialog(_mActivity,message)
    }

    override fun launchActivity(intent: Intent) {
        ArmsUtils.startActivity(intent)
    }

}
