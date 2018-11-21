package com.example.aalap.news.models.weathermodels

import com.example.aalap.news.R
import com.google.gson.annotations.SerializedName
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmObject
import java.text.SimpleDateFormat
import java.util.*

open class DailyData(@SerializedName("time") var time: Long = 0,
                     @SerializedName("summary") var summary: String = "",
                     @SerializedName("icon") var icon: String = "",
                     @SerializedName("temperatureMin") var temperatureMin: Double = 0.0,
                     @SerializedName("temperatureMax") var temperatureMax: Double = 0.0
) :RealmObject() {
    fun temperatureMin(): Int {
        return getCelsius(temperatureMin)
    }

    fun temperatureMax(): Int {
        return getCelsius(temperatureMax)
    }

    fun getIconRes(): Int {
        return getIcon(icon)
    }

    fun getDayOfTheWeek(): String {
        var formatter = SimpleDateFormat("EEEE")
        formatter.timeZone = TimeZone.getDefault()
        var dateTime = Date(time * 1000.toLong())
        return formatter.format(dateTime)
    }
}


open class HourlyData(@SerializedName("time") var time: Long = 0,
                      @SerializedName("icon") var icon: String = "",
                      @SerializedName("temperature") var temperature: Double = 0.0,
                      @SerializedName("apparentTemperature") var apparentTemperature: Double = 0.0,
                      @SerializedName("summary") var summary: String = ""
):RealmObject() {
    fun temperature(): Int {
        return getCelsius(temperature)
    }

    fun feelsLike(): Int {
        return getCelsius(apparentTemperature)
    }

    fun getIconRes(): Int {
        return getIcon(icon)
    }

    fun getTimeAsHour(): String {
        var formatter = SimpleDateFormat("h a")
        formatter.timeZone = TimeZone.getDefault()
        var dateTime = Date(time * 1000.toLong())
        return formatter.format(dateTime)
    }
}

open class Hourly(@SerializedName("icon") var icon: String = "",
                  @SerializedName("data") var data: RealmList<HourlyData> = RealmList()
):RealmObject()

open class Daily(@SerializedName("icon") var icon: String = "",
                 @SerializedName("data") var data: RealmList<DailyData> = RealmList()
): RealmObject()

open class Currently(@SerializedName("time") var time: Long = 0,
                     @SerializedName("icon") var icon: String = "",
                     @SerializedName("temperature") var temperature: Double = 0.0,
                     @SerializedName("apparentTemperature") var apparentTemperature: Double = 0.0

):RealmObject() {
    fun currentTemperature(): Int {
        return getCelsius(temperature)
    }

    fun feelsLike(): Int {
        return getCelsius(apparentTemperature)
    }

    fun getIconRes(): Int {
        return getIcon(icon)
    }
}

open class Weather(@SerializedName("currently") var currently: Currently? = Currently(0, "", 0.0, 0.0),
                   @SerializedName("daily") var daily: Daily? = Daily("", RealmList()),
                   @SerializedName("hourly") var hourly: Hourly? = Hourly("", RealmList())
) : RealmObject()


fun getCelsius(fahrenheit: Double): Int {
    return ((fahrenheit - 32) * 5 / 9).toInt()
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

