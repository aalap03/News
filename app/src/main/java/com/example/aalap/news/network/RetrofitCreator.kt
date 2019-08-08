package com.example.aalap.news.network

import com.example.aalap.news.network.service.IRegionService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitCreator {

    private val BASE_URL = "https://org.e-cobalt.com"

    fun getDomainServiceWithoutOrigin(): IRegionService {
        return getSimpleRetrofit().create(IRegionService::class.java)
    }

    fun <S> getService(service: Class<S>): S {

        if (service is IRegionService) {
            throw RuntimeException("Region service can not be available with this retrofit client, please use method getDomainServiceWithoutOrigin() instead")
        }

        return getRetrofit().create(service)
    }

    private fun getRetrofit(): Retrofit {
        return getRetrofitBuilder()
                .client(client())
                .build()
    }

    private fun getSimpleRetrofit(): Retrofit {
        return getRetrofitBuilder()
                .build()
    }

    private fun getRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
    }


    private fun client(): OkHttpClient {

        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC

        return OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor { chain ->
                    chain.proceed(chain
                            .request()
                            .newBuilder()
                            .addHeader("Origin", "https://demo.e-cobalt.com")
                            .build()
                    )
                }
                .build()
    }
}