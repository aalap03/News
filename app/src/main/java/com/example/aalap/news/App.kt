package com.example.aalap.news

import android.app.Application
import com.example.aalap.news.retrofitutils.NewsService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

lateinit var retrofit: Retrofit
lateinit var retrofitService: NewsService

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .build()

        retrofitService = retrofit.create(NewsService::class.java)
    }

    fun okHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor())
                .build()
    }

    fun interceptor(): Interceptor {
        return Interceptor { chain ->

            val request = chain.request()
            val newBuilder = request.newBuilder()
            newBuilder.header("Authorization", "Bearer ${getString(R.string.news_api_key)}")

            chain.proceed(newBuilder.build())
        }
    }
}