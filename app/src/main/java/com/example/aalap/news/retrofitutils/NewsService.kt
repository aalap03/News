package com.example.aalap.news.retrofitutils

import com.example.aalap.news.models.Article
import com.example.aalap.news.models.News
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface NewsService {

    //https://newsapi.org/v2/top-headlines?country=us&apiKey=cfce766b2dea4a6f91253e26fead9721
    @GET("top-headlines")
    fun getTopHeadlinesByCountry(@Query("country") country: String): Single<Response<News>>

    @GET("top-headlines")
    fun getTopHeadlinesByCategory(@Query("category") country: String): Single<Response<News>>
}