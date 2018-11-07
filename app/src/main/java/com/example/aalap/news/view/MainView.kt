package com.example.aalap.news.view

import com.example.aalap.news.models.weathermodels.Currently
import com.example.aalap.news.models.weathermodels.DailyData

interface MainView {

    fun showDailyData(list: List<DailyData>)

    fun showCurrentWeather(current: Currently)

    fun dailyDataError(errorMsg: String)
}