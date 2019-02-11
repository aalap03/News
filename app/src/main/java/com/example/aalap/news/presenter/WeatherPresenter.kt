package com.example.aalap.news.presenter

import com.example.aalap.news.models.weathermodels.WeatherModel
import com.example.aalap.news.pref
import com.example.aalap.news.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger

class WeatherPresenter(var view: MainView, var model: WeatherModel) : AnkoLogger {

    var compositeDisposable = CompositeDisposable()

    fun getCurrentWeather(latitude: Double, longitude: Double) {

        view.weatherLoading()

        pref.saveLastCoOrdinates(latitude.toString(), longitude.toString())
        model.getCurrentWeather(latitude, longitude)
                .observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe { t1, t2 ->
                    t1?.let { view.getWeatherData(weather = it) }
                    t2?.let { view.error(it.localizedMessage) }
                }?.let {
                    compositeDisposable.add(it)
                }
    }
}