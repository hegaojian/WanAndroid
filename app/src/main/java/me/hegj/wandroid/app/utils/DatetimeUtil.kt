package me.hegj.wandroid.app.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * hgj
 */

object DatetimeUtil {

    val DATE_PATTERN = "yyyy-MM-dd"
    var DATE_PATTERN_SS = "yyyy-MM-dd HH:mm:ss"
    var DATE_PATTERN_MM = "yyyy-MM-dd HH:mm"

    /**
     * 获取现在时刻
     */
    val now: Date
        get() = Date(Date().time)
    /**
     * 获取现在时刻
     */
    val nows: Date
        get() = formatDate(DATE_PATTERN, now)

    /**
     * Date to Strin
     */
    fun formatDate(date: Date?, formatStyle: String): String {
        if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            return sdf.format(date)
        } else {
            return ""
        }

    }
    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long, formatStyle: String): String {
        val sdf = SimpleDateFormat(formatStyle)
        return sdf.format(Date(date))

    }
    /**
     * Date to Date
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(formatStyle: String, date: Date?): Date {
        if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            val formatDate = sdf.format(date)
            try {
                return sdf.parse(formatDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                return Date()
            }

        } else {
            return Date()
        }
    }

    /**
     * 将时间戳转换为时间
     */
    fun stampToDate(s: String): Date {
        val lt = s.toLong()
        return Date(lt)
    }


}
