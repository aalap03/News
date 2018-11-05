package com.example.aalap.news.models.newsmodels

import com.example.aalap.news.newsService
import io.reactivex.Single

val TAG = "Model:"
class NewsModel {


    fun getTopHeadlinesByCountry(): Single<List<Article>>? {
        return newsService.getTopHeadlinesByCountry("us")
                .map { it.body()?.articles }
    }

    fun getTopHeadlinesByCategory(category: String): Single<List<Article>>? {
        return newsService.getTopHeadlinesByCategory(category)
                .map { it.body()?.articles }
    }

    fun getEverything(category: String, page: Long, pageSize: Int): Single<List<Article>>? {
        return newsService.getEverything(category)
                .map { it.body()?.articles }
    }

    fun getTopHeadlinesByCountryAndCategory(country: String, category: String): Single<List<Article>>? {
        return newsService.getTopHeadlinesByCategoryAndCountry(country, category)
                .map { it.body()?.articles }
    }
}