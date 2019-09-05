package me.hegj.wandroid.app.utils

import android.net.ParseException
import android.text.TextUtils
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import okhttp3.MediaType
import org.json.JSONException
import retrofit2.HttpException
import timber.log.Timber
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


object HttpUtils {
    var mediaType = MediaType.parse("application/json;charset=utf-8")

    fun getErrorText(t: Throwable): String{
        Timber.tag("Catch-Error").w(t)
        //这里不光只能打印错误, 还可以根据不同的错误做出不同的逻辑处理
        //这里只是对几个常用错误进行简单的处理, 展示这个类的用法, 在实际开发中请您自行对更多错误进行更严谨的处理
        var msg = t.message
        if (t is UnknownHostException) {
            msg = "网络不可用，请检查网络后重试"
        } else if (t is IllegalArgumentException) {
            msg = "非法数据异常"
        } else if (t is SocketTimeoutException) {
            msg = "请求网络超时，请检查网络后重试"
        } else if (t is HttpException) {
            msg = convertStatusCode(t)
        } else if (t is JsonParseException || t is ParseException || t is JSONException || t is JsonIOException) {
            msg = "数据解析错误"
        }
        return if(TextUtils.isEmpty(msg)) "请求失败，请稍后再试" else msg!!
    }

    private fun convertStatusCode(httpException: HttpException): String {
        val msg: String
        if (httpException.code() == 500) {
            msg = "服务器发生错误"
        } else if (httpException.code() == 404) {
            msg = "请求地址不存在"
        } else if (httpException.code() == 403) {
            msg = "请求被服务器拒绝"
        } else if (httpException.code() == 307) {
            msg = "请求被重定向到其他页面"
        } else {
            msg = httpException.message()
        }
        return msg
    }

    /**
     * 整合cookie为唯一字符串
     */
     fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        for (cookie in cookies) {
            val arr = cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (s in arr) {
                if (set.contains(s)) {
                    continue
                }
                set.add(s)

            }
        }

        for (cookie in set) {
            sb.append(cookie).append(";")
        }

        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }

        return sb.toString()
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
     fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim({ it <= ' ' })
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (exception: IOException) {
                exception.printStackTrace()
            }

        }
        return null
    }
}
