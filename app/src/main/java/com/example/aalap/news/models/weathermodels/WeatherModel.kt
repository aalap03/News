package com.example.aalap.news.models.weathermodels

import com.example.aalap.news.weatherService
import io.reactivex.Single
import io.reactivex.SingleSource
import io.realm.Realm
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response
import java.lang.RuntimeException

class WeatherModel : AnkoLogger {

    fun getCurrentWeather(latitude: Double, longitude: Double): Single<Weather?> {
        return weatherService.getCurrentWeather(latitude, longitude)
                .map { it.body() }
                .doOnSuccess { t -> saveToDB(weather = t) }
                .onErrorReturnItem(Realm.getDefaultInstance().where(Weather::class.java).findFirst()
                        ?: Weather())
    }

    private fun saveToDB(weather: Weather?): Single<Weather?> {
        Realm.getDefaultInstance()
                .use {
                    it.executeTransaction { realm ->
                        realm.where(Weather::class.java).findAll().deleteAllFromRealm()
                        realm.where(Hourly::class.java).findAll().deleteAllFromRealm()
                        realm.where(HourlyData::class.java).findAll().deleteAllFromRealm()
                        realm.where(Currently::class.java).findAll().deleteAllFromRealm()
                        realm.where(Daily::class.java).findAll().deleteAllFromRealm()
                        realm.where(DailyData::class.java).findAll().deleteAllFromRealm()

                        realm.copyToRealm(weather)
                    }
                }

        return Single.just(weather)
    }
}