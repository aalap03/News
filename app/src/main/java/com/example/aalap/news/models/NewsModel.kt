package com.example.aalap.news.models

import com.example.aalap.news.retrofitService
import io.reactivex.Single

val TAG = "Model:"
class NewsModel {


    fun getTopHeadlinesByCountry(): Single<List<Article>>? {

        return retrofitService.getTopHeadlinesByCountry("us")
                .map { it.body()?.articles }
    }
}