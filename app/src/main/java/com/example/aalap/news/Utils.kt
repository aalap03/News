package com.example.aalap.news

import android.content.res.Resources
import android.util.TypedValue
import com.afollestad.materialdialogs.MaterialDialog
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Utils {

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getLocaleTime(defaultTime: String): String {

        var desiredOutput = ""
        val utc = TimeZone.getTimeZone("UTC")
        var f = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        f.timeZone = utc
        val cal = GregorianCalendar(utc)
        try {
            cal.time = f.parse(defaultTime)
            f = SimpleDateFormat("EEE dd-MMM, yyyy HH:mm", Locale.getDefault())
            desiredOutput = f.format(cal.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            desiredOutput = defaultTime
        }

        return desiredOutput
    }

    fun dialog() {

    }
}