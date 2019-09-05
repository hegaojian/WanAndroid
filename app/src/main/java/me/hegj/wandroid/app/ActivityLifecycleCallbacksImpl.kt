/*
 * Copyright 2017 JessYan
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

import android.app.Activity
import android.app.Application
import android.os.Bundle

import timber.log.Timber

/**
 * ================================================
 * 展示 [Application.ActivityLifecycleCallbacks] 的用法
 * Created by JessYan on 04/09/2017 17:14
 * [Contact me](mailto:jess.yan.effort@gmail.com)
 * [Follow me](https://github.com/JessYanCoding)
 * ================================================
 */
class ActivityLifecycleCallbacksImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity?) {
        Timber.i("$activity - onActivityStarted")
    }

    override fun onActivityResumed(activity: Activity?) {
        Timber.i("$activity - onActivityResumed")
    }

    override fun onActivityPaused(activity: Activity?) {
        Timber.i("$activity - onActivityPaused")
    }

    override fun onActivityStopped(activity: Activity?) {
        Timber.i("$activity - onActivityStopped")
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        Timber.i("$activity - onActivitySaveInstanceState")
    }

    override fun onActivityDestroyed(activity: Activity?) {
        Timber.i("$activity - onActivityDestroyed")
        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
    }
}
