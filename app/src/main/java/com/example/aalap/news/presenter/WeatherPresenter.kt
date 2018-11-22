package com.example.aalap.news.presenter

import com.example.aalap.news.App
import com.example.aalap.news.Pref
import com.example.aalap.news.models.weathermodels.WeatherModel
import com.example.aalap.news.pref
import com.example.aalap.news.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class WeatherPresenter(var view: MainView, var model: WeatherModel) : AnkoLogger {

    var compositeDisposable = CompositeDisposable()

    fun getCurrentWeather(latitude: Double, longitude: Double) {

        info { "Weather: requesting... $latitude : $longitude" }

        pref.saveLastCoOrdinates(latitude.toString(), longitude.toString())
        model.getCurrentWeather(latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe { t1, t2 ->
                    when {
                        t1 != null -> {
                            view.getWeatherData(weather = t1)
                        }
                        t2 != null -> {
                            view.error(t2.localizedMessage)
                        }
                        else -> {
                            view.error("Not sure what the fuck is wrong")
                        }
                    }
                }?.let {
                    compositeDisposable.add(it)
                }
    }
}