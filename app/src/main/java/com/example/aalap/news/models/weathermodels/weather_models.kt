package com.example.aalap.news.models.weathermodels

import android.util.Log
import com.example.aalap.news.R
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class DailyData(@SerializedName("time") val time: Long,
                     @SerializedName("summary") val summary: String,
                     @SerializedName("icon") val icon: String,
                     @SerializedName("temperatureMin") val temperatureMin: Double,
                     @SerializedName("temperatureMax") val temperatureMax: Double
) {
    fun temperatureMin(): Int {
        return getCelcious(temperatureMin)
    }

    fun temperatureMax(): Int {
        return getCelcious(temperatureMax)
    }

    fun getIconRes(): Int {
        return getIcon(icon)
    }

    fun getDayOfTheWeek(): String {
        val formatter = SimpleDateFormat("EEEE")
        formatter.timeZone = TimeZone.getDefault()
        val dateTime = Date(time * 1000.toLong())
        return formatter.format(dateTime)
    }
}


data class HourlyData(@SerializedName("time") val time: Long,
                      @SerializedName("icon") val icon: String,
                      @SerializedName("temperature") val temperature: Double,
                      @SerializedName("apparentTemperature") val apparentTemperature: Double,
                      @SerializedName("summary") val summary: String
) {
    fun temperature(): Int {
        return getCelcious(temperature)
    }

    fun feelsLike(): Int {
        return getCelcious(apparentTemperature)
    }

    fun getIconRes(): Int {
        return getIcon(icon)
    }

    fun getTimeAsHour(): String {
        val formatter = SimpleDateFormat("h a")
        formatter.timeZone = TimeZone.getDefault()
        val dateTime = Date(time * 1000.toLong())
        return formatter.format(dateTime)
    }
}

data class Hourly(@SerializedName("icon") val icon: String,
                  @SerializedName("data") val data: List<HourlyData>
)

data class Daily(@SerializedName("icon") val icon: String,
                 @SerializedName("data") val data: List<DailyData>
) {
    fun getIconRes(): Int {
        return getIcon(icon)
    }
}

data class Currently(@SerializedName("time") val time: Long,
                     @SerializedName("icon") val icon: String,
                     @SerializedName("temperature") val temperature: Double,
                     @SerializedName("apparentTemperature") val apparentTemperature: Double
) {
    fun currentTemperature(): Int {
        return getCelcious(temperature)
    }

    fun feelsLike(): Int {
        return getCelcious(apparentTemperature)
    }

    fun getIconRes(): Int{
        return getIcon(icon)
    }
}

data class Weather(@SerializedName("currently") val currently: Currently,
                   @SerializedName("daily") val daily: Daily,
                   @SerializedName("hourly") val hourly: Hourly
)


fun getCelcious(ferenheite: Double): Int {
    return ((ferenheite - 32) * 5 / 9).toInt()
}

//clear-day, clear-night, rain, snow, sleet, wind, fog, cloudy, partly-cloudy-day, or partly-cloudy-night
fun getIcon(icon: String): Int {
    return when (icon) {
        "clear-day" -> R.drawable.ic_clear_day

        "clear-night" -> R.drawable.ic_clear_night

        "rain" -> R.drawable.ic_rain

        "snow" -> R.drawable.ic_snowy

        "sleet" -> R.drawable.ic_sleet

        "wind" -> R.drawable.ic_windy

        "fog" -> R.drawable.ic_foggy

        "cloudy" -> R.drawable.ic_cloudy

        "partly-cloudy-day" -> R.drawable.ic_cloudy_day

        "partly-cloudy-night" -> R.drawable.ic_cloudy_night

        else -> R.mipmap.ic_launcher
    }
}

