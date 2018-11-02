package com.example.aalap.news.retrofitutils

import com.example.aalap.news.models.newsmodels.News
import com.example.aalap.news.models.weathermodels.Weather
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {

    @GET("{latitude}, {longitude}")
    fun getCurrentWeather(@Path("latitude") latitude: Double,
                          @Path("longitude") longitude: Double): Single<Response<Weather>>
}