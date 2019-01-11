package com.example.aalap.news

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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

    inner class Decorator(context: Context) : RecyclerView.ItemDecoration() {

        var divider: Drawable? = ContextCompat.getDrawable(context, R.drawable.line_divider)

        override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            super.onDraw(canvas, parent, state)
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight
            val childCount = parent.childCount

            for (i in 0 until childCount) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams
                val top = child.bottom + params.height
                val bottom = top + (divider?.intrinsicHeight ?: 0)
                divider?.setBounds(left, top, right, bottom)
                divider?.draw(canvas)
            }
        }
    }
}