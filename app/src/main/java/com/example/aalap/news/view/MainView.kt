package com.example.aalap.news.view

import com.example.aalap.news.models.weathermodels.Weather

interface MainView {

    fun error(errorMsg: String)

    fun getWeatherData(weather: Weather)

    fun weatherLoading()
}