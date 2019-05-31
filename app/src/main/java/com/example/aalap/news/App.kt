package com.example.aalap.news

import android.app.Application
import com.example.aalap.news.retrofitutils.NewsService
import com.example.aalap.news.retrofitutils.WeatherService
import io.realm.Realm
import io.realm.RealmConfiguration
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

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
        val config = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("news.realm")
                .build()
        Realm.setDefaultConfiguration(config)

        newsRetrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .build()

        weatherRetrofit = Retrofit.Builder()
                .baseUrl("https://api.darksky.net/forecast/" + BuildConfig.WEATHER_API_KEY + "/")
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
            newBuilder.header("Authorization", "Bearer ${BuildConfig.NEWS_API_KEY}")

            chain.proceed(newBuilder.build())
        }
    }
}