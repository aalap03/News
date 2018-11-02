package com.example.aalap.news.retrofitutils

import com.example.aalap.news.models.newsmodels.News
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=cfce766b2dea4a6f91253e26fead9721
    @GET("top-headlines")
    fun getTopHeadlinesByCountry(@Query("country") country: String): Single<Response<News>>

    @GET("top-headlines")
    fun getTopHeadlinesByCategory(@Query("category") country: String): Single<Response<News>>

    @GET("top-headlines")
    fun getTopHeadlinesByCategoryAndCountry(@Query("country") country: String,
                                            @Query("category") category: String): Single<Response<News>>

    @GET("everything")
    fun getEverything(
            @Query("q") query: String,
            @Query("page") page: Long,
            @Query("pageSize") size: Int): Single<Response<News>>
}