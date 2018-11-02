package com.example.aalap.news.presenter

import com.example.aalap.news.models.weathermodels.WeatherModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class WeatherPresenter(var model: WeatherModel) {

    fun getCurrentWeather(latitude: Double, longitude: Double) {

//        val subscribe = model.getCurrentWeather(latitude, longitude)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe { t1, t2 ->
//                    when {
//                        t1 != null -> {
//
//                        }
//                        t2 != null -> {
//
//                        }
//                        else -> {
//
//                        }
//                    }
//                }
    }
}