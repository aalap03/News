package com.example.aalap.news.models.weathermodels

import com.example.aalap.news.weatherService
import io.reactivex.Single
import retrofit2.Response

class WeatherModel {

    fun getCurrentWeather(latitude: Double, longitude: Double): Single<Weather>? {
        return weatherService.getCurrentWeather(latitude, longitude)
                .map { it.body() }
    }
}