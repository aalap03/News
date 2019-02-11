package com.example.aalap.news.models.newsmodels

import com.example.aalap.news.newsService
import com.example.aalap.news.pref
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.Sort
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response
import java.net.UnknownHostException

class NewsModel : AnkoLogger {

    lateinit var realm: Realm

    fun getHeadlinesWithCountryAndCategory(category: String): Observable<NewsResult> {

        val country = Country.countryKeyValueMap()[pref.getCountry()]
        return newsService.getTopHeadlinesByCountryAndCategory(country!!, category)
                .map<NewsResult> { NewsResult.NewsData(saveAndGetNewsByCategory(it, category)) }
                .onErrorReturn { NewsResult.NewsError(onError(throwable = it), getSavedNewsByCategory(category)) }
    }

    fun getSearchedHeadlines(query: String): Single<NewsResult> {
        return newsService.getSearchedHeadlines(query)
                .map<NewsResult> {
                    if (it.isSuccessful)
                        it.body()?.articles?.let { articles -> NewsResult.NewsData(articles) }
                    else
                        it.errorBody()?.string()?.let { errorMsg -> NewsResult.NewsError(errorMsg, arrayListOf()) }
                }
                .onErrorReturn { NewsResult.NewsError(onError(throwable = it), arrayListOf()) }
    }

    private fun saveAndGetNewsByCategory(response: Response<ResponseBody>, category: String): MutableList<Article> {

        val list = mutableListOf<Article>()
        realm = Realm.getDefaultInstance()
        realm.use {
            it.executeTransaction { realm ->
                val res = response.body()?.string()

                realm.where(News::class.java).findFirst()?.deleteFromRealm()
                realm.where(Article::class.java)
                        .equalTo("category", category)
                        .equalTo("isSaved", false)
                        .findAll()
                        .deleteAllFromRealm()

                realm.createObjectFromJson(News::class.java, res)
                val articles = realm
                        .where(News::class.java)
                        .findFirst()
                        ?.articles
                        ?.sort("publishedAt", Sort.DESCENDING)

                articles?.forEach { article ->
                    article.category = category
                    list.add(realm.copyFromRealm(article))
                }
            }

        }

        return list
    }

    fun getSavedNewsByCategory(category: String): MutableList<Article> {

        val list = mutableListOf<Article>()
        realm = Realm.getDefaultInstance()

        realm.use {
            it.where(Article::class.java)
                    .equalTo("category", category)
                    .findAll().forEach { article ->
                        list.add(realm.copyFromRealm(article))
                    }
        }

        info { "Article: db ${list.size}" }
        return list
    }

    private fun onError(throwable: Throwable): String {
        throwable.printStackTrace()
        return if (throwable is UnknownHostException) "No internet" else throwable.localizedMessage
    }
}