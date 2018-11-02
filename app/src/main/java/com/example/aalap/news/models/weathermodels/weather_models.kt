package com.example.aalap.news.models.weathermodels

import com.example.aalap.news.R
import com.google.gson.annotations.SerializedName

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
}


data class HourlyData(@SerializedName("time") val time: Long,
                      @SerializedName("icon") val icon: String,
                      @SerializedName("temperature") val temperature: Double,
                      @SerializedName("apparentTemperature") val apparentTemperature: Double
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
}

data class Weather(@SerializedName("currently") val currently: Currently,
                   @SerializedName("daily") val daily: Daily,
                   @SerializedName("hourly") val hourly: Hourly
)


fun getCelcious(ferenheite: Double): Int {
    return ((ferenheite - 32) * 5 / 9).toInt()
}

fun getIcon(icon: String): Int {
//        when (icon) {
//            "clear-day" -> R.drawable.wclear_day
//
//            "clear-night" -> R.drawable.wclear_night
//
//            "cloudy" -> R.drawable.wcloudy
//
//            "partly-cloudy-day" -> R.drawable.wcloudy
//
//            "cloudy-night" -> R.drawable.wcloudy_night
//
//            "partly-cloudy-night" -> R.drawable.wcloudy_night
//
//            "fog" -> R.drawable.wfog
//
//            "wind" -> R.drawable.wind
//
//            "sleet" -> R.drawable.wind
//
//            "partly-cloudy" -> R.drawable.wpartly_cloudy
//
//            "rain" -> R.drawable.wrain
//
//            "snow" -> R.drawable.wsnow
//
//            else -> R.drawable.wclear_day
    //       }
    return R.mipmap.ic_launcher
}

