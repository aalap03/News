package com.example.aalap.news.models.weathermodels

import com.example.aalap.news.weatherService
import io.reactivex.Single
import retrofit2.Response

class WeatherModel {

    fun getCurrentWeather(latitude: Double, longitude: Double): Single<Weather>? {
        return weatherService.getCurrentWeather(latitude, longitude)
                .map { it.body() }
    }

    fun getDailyData(latitude: Double, longitude: Double): Single<List<DailyData>>? {
        return getCurrentWeather(latitude, longitude)
                ?.map { it.daily.data }
    }

    fun getHourlyData(latitude: Double, longitude: Double) : Single<List<HourlyData>>? {
        return getCurrentWeather(latitude, longitude)
                ?.map { it.hourly.data }
    }
}