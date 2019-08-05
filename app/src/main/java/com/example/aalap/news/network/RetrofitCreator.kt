package com.example.aalap.news.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCreator {

    private val BASE_URL = "https://org.e-cobalt.com"

    fun <S> getService(service: Class<S>): S {
        return getRetrofit().create(service)
    }

    fun getSimpleRetrofit(origin: String? = null): Retrofit {

        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)


        if (origin != null)
            retrofit.client(clientWithOrigin(origin))
        else
            retrofit.client(getOkHttpClient())

        return retrofit.build()
    }

    private fun clientWithOrigin(origin: String): OkHttpClient {

        return OkHttpClient.Builder()
                .addInterceptor(getInterceptor(origin))
                .build()
    }

    private fun getRetrofit(): Retrofit {

        return Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
    }

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()

                .build()
    }

    private fun getInterceptor(origin: String): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val request = chain.request()
            val newBuilder = request.newBuilder()

            newBuilder.header("Origin", origin)
            if (!response.isSuccessful) {
                if (response.code() == 401 || response.code() == 403) {
                    "Invalid credentials"
                }
            }
            response
        }
    }
}