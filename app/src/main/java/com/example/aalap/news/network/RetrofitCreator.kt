package com.example.aalap.news.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {

    private val BASE_URL = "https://org.e-cobalt.com"

    fun <S> getService(service: Class<S>): S {
        return if (service is IDomainAndAuthService)
            getSimpleRetrofit().create(service)
        else
            getRetrofit().create(service)
    }

    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }

    private fun getSimpleRetrofit(): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }

    private fun client(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build()
    }
}