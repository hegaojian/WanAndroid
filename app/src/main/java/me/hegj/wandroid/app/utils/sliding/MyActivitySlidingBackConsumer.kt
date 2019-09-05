package me.hegj.wandroid.app.utils.sliding

import android.app.Activity
import com.billy.android.swipe.consumer.ActivitySlidingBackConsumer
import me.hegj.wandroid.app.utils.SettingUtil

/**
 * 自定义滑动返回的 Consum 继承 修改逻辑，当左滑时判断当前有没有开启侧滑返回，没有的话，不允许触发滑动
 */
class MyActivitySlidingBackConsumer constructor(activity:Activity) :ActivitySlidingBackConsumer(activity) {
    override fun tryAcceptMoving(pointerId: Int, downX: Float, downY: Float, dx: Float, dy: Float): Boolean {
        return if(SettingUtil.getSlidable(mActivity)){
            super.tryAcceptMoving(pointerId, downX, downY, dx, dy)
        }else{
            false
        }


    }
}