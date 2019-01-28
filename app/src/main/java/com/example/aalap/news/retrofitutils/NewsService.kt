package com.example.aalap.news.retrofitutils

import com.example.aalap.news.models.newsmodels.News
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {


    @GET("top-headlines")
    fun getTopHeadlinesByCountryAndCategory(@Query("country") country: String,
                                            @Query("category") category: String): Observable<Response<ResponseBody>>

    @GET("everything")
    fun getEverything(@Query("q") query: String): Single<Response<News>>
}