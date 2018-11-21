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
                .doOnSuccess { t -> saveToDB(it = t) }
                .onErrorReturnItem(Realm.getDefaultInstance().where(Weather::class.java).findFirst() ?: Weather())
    }

    private fun saveToDB(it: Weather?): Single<Weather?> {
        Realm.getDefaultInstance().executeTransaction { realm ->
            realm.where(Weather::class.java).findAll().deleteAllFromRealm()
            realm.where(Hourly::class.java).findAll().deleteAllFromRealm()
            realm.where(HourlyData::class.java).findAll().deleteAllFromRealm()
            realm.where(Currently::class.java).findAll().deleteAllFromRealm()
            realm.where(Daily::class.java).findAll().deleteAllFromRealm()
            realm.where(DailyData::class.java).findAll().deleteAllFromRealm()

            realm.copyToRealm(it)

            testLogs(realm)
        }
        return Single.just(it)
    }

//    fun <T> returnOnError(): T  {
//        val weather = Realm.getDefaultInstance().where(Weather::class.java).findFirst()
//        if(weather == null)
//            return "Weather was not found bro"
//        else
//            return weather
//    }

    private fun testLogs(realm: Realm) {
        info { "Weather: ${realm.where(Weather::class.java).findAll().size}" }
        info { "Weather: daily ${realm.where(DailyData::class.java).findAll().size}" }
        info { "Weather: hourly ${realm.where(HourlyData::class.java).findAll().size}" }
    }
}