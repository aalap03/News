package com.example.aalap.news.view

import com.example.aalap.news.models.weathermodels.Currently
import com.example.aalap.news.models.weathermodels.DailyData
import com.example.aalap.news.models.weathermodels.HourlyData

interface MainView {

    fun showDailyData(list: List<DailyData>)

    fun showCurrentWeather(current: Currently)

    fun error(errorMsg: String)

    fun hourlyData(data: List<HourlyData>)
}