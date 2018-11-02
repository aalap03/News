package com.example.aalap.news

import android.app.Application
import com.example.aalap.news.retrofitutils.NewsService
import com.example.aalap.news.retrofitutils.WeatherService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

lateinit var newsRetrofit: Retrofit
lateinit var weatherRetrofit: Retrofit
lateinit var newsService: NewsService
lateinit var weatherService: WeatherService
lateinit var pref: Pref

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        newsRetrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .build()

        weatherRetrofit = Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/"+getString(R.string.weather_api_key)+"/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        pref = Pref(this)

        newsService = newsRetrofit.create(NewsService::class.java)
        weatherService = weatherRetrofit.create(WeatherService::class.java)
    }

    private fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor())
                .build()
    }

    private fun interceptor(): Interceptor {
        return Interceptor { chain ->

            val request = chain.request()
            val newBuilder = request.newBuilder()
            newBuilder.header("Authorization", "Bearer ${getString(R.string.news_api_key)}")

            chain.proceed(newBuilder.build())
        }
    }
}