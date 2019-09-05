package me.hegj.wandroid.app.utils

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.Gravity
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import me.hegj.wandroid.R

object ShowUtils {
    private var dialog: ProgressDialog? = null
    private var toast: Toast? = null
    fun showLoading(context: Context) {
        dialog?.run {
            if (isShowing) return
        }
        dialog = ProgressDialog(context)
        dialog?.run {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCanceledOnTouchOutside(false)
            setProgressStyle(ProgressDialog.STYLE_SPINNER)
            setMessage("请求网络中...")
            val d = ClipDrawable(ColorDrawable(SettingUtil.getColor(context)), Gravity.LEFT, ClipDrawable.HORIZONTAL)
            setProgressDrawable(d)
            show()
        }
    }

    fun dismissLoading() {
        dialog?.run {
            if (isShowing) {
                dismiss()
            }
        }
        dialog = null
    }

    fun showDialog(activity: Activity, message: String) {
        if (!activity.isFinishing) {
            MaterialDialog(activity).show {
                title(R.string.title)
                message(text = message)
                positiveButton(R.string.confirm)
            }
        }

    }

    fun showDialog(activity: Activity, message: String, title: String) {
        if (!activity.isFinishing) {
            MaterialDialog(activity).show {
                title(text = title)
                message(text = message)
                positiveButton(R.string.confirm)
            }
        }

    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment)
     */
    fun hideSoftKeyboard(activity: Activity) {
        val view = activity.currentFocus
        view?.let {
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

    }


    /**
     * 在屏幕中间吐司
     */
    fun showToastCenter(context: Context, arg: String) {
        if (!TextUtils.isEmpty(arg)) {
            toast?.cancel()
            toast = Toast.makeText(context.applicationContext, arg, Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
                show()
            }
        }

    }

    /**
     * 在屏幕底部吐司 默认
     */
    fun showToast(context: Context, arg: String) {
        if (!TextUtils.isEmpty(arg)) {
            toast?.cancel()
            toast = Toast.makeText(context.applicationContext, arg, Toast.LENGTH_SHORT).apply {
                show()
            }
        }

    }

    /**
     * 隐藏toast
     */
    fun hide() {
        toast?.let {
            it.cancel()
        }
    }


}
