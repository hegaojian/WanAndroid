/*
 * Copyright 2018 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.hegj.wandroid.app

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import android.view.View
import com.jess.arms.integration.cache.IntelligentCache
import com.jess.arms.utils.ArmsUtils
import com.squareup.leakcanary.RefWatcher
import timber.log.Timber

/**
 * ================================================
 * 展示 [FragmentManager.FragmentLifecycleCallbacks] 的用法
 *
 *
 * Created by JessYan on 23/08/2018 17:14
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class FragmentLifecycleCallbacksImpl : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        Timber.i("$f - onFragmentAttached")
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("$f - onFragmentCreated")
        // 在配置变化的时候将这个 Fragment 保存下来,在 Activity 由于配置变化重建时重复利用已经创建的 Fragment。
        // https://developer.android.com/reference/android/app/Fragment.html?hl=zh-cn#setRetainInstance(boolean)
        // 如果在 XML 中使用 <Fragment/> 标签,的方式创建 Fragment 请务必在标签中加上 android:id 或者 android:tag 属性,否则 setRetainInstance(true) 无效
        // 在 Activity 中绑定少量的 Fragment 建议这样做,如果需要绑定较多的 Fragment 不建议设置此参数,如 ViewPager 需要展示较多 Fragment
        f.retainInstance = true
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        Timber.i("$f - onFragmentViewCreated")
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        Timber.i("$f - onFragmentActivityCreated")
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentStarted")
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentResumed")
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentPaused")
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentStopped")
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        Timber.i("$f - onFragmentSaveInstanceState")
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentViewDestroyed")
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentDestroyed")
        f.activity?.let {
            (ArmsUtils
                    .obtainAppComponentFromContext(it)
                    .extras()
                    .get(IntelligentCache.getKeyOfKeep(RefWatcher::class.java.name)) as RefWatcher)
                    .watch(f)
        }

    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        Timber.i("$f - onFragmentDetached")
    }
}
