package com.example.aalap.news.presenter

import com.example.aalap.news.models.weathermodels.WeatherModel
import com.example.aalap.news.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class WeatherPresenter(var view: MainView, var model: WeatherModel): AnkoLogger {

    var compositeDisposable = CompositeDisposable()

    fun getCurrentWeather(latitude: Double, longitude: Double) {

        info { "Weather: requesting..." }

        model.getCurrentWeather(latitude, longitude)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe { t1, t2 ->
                    when {
                        t1 != null -> {
                            view.showDailyData(t1.daily.data)
                            view.showCurrentWeather(t1.currently)
                            view.hourlyData(t1.hourly.data)
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