package me.hegj.wandroid.app.weight.loadCallBack


import com.kingja.loadsir.callback.Callback

import me.hegj.wandroid.R


/**
 * Description:TODO
 * Create Time:2017/9/4 10:20
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */

class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }
}
