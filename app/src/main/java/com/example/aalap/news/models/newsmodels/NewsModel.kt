package com.example.aalap.news.models.newsmodels

import com.example.aalap.news.newsService
import com.example.aalap.news.pref
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response

class NewsModel : AnkoLogger {

    fun getHeadlinesWithCountryAndCategory(category: String): Observable<NewsResult> {

        val country = Country.countryKeyValueMap()[pref.getCountry()]

        return newsService.getTopHeadlinesByCountryAndCategory(country!!, category)
                .map <NewsResult> { NewsResult.NewsData(saveAndGetNewsByCategory(it, category))}
                .onErrorReturn { NewsResult.NewsError(it.localizedMessage, getSavedNewsByCategory(category)) }
    }

    fun getSearchedHeadlines(query: String): Single<NewsResult> {
        return newsService.getEverything(query)
                .map<NewsResult> {
                    if (it.isSuccessful)
                        it.body()?.articles?.let { articles -> NewsResult.NewsData(articles) }
                    else
                        it.errorBody()?.string()?.let { errorMsg -> NewsResult.NewsError(errorMsg, arrayListOf()) }
                }
                .onErrorReturn { NewsResult.NewsError(it.localizedMessage, arrayListOf()) }
    }

    private fun saveAndGetNewsByCategory(response: Response<ResponseBody>, category: String): List<Article>{

        val list = mutableListOf<Article>()
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val res = response.body()?.string()

                realm.where(News::class.java).findFirst()?.deleteFromRealm()
                realm.where(Article::class.java).equalTo("category", category).findAll().deleteAllFromRealm()

                realm.createObjectFromJson(News::class.java, res)
                val articles = Realm.getDefaultInstance().where(News::class.java).findFirst()?.articles
                info { "category: $category ----------->>>>" }
                articles?.forEach { article ->
                    article.category = category
                    list.add(realm.copyFromRealm(article))
                    info { "article: $article" }
                }
                info { "category: $category saved----->>>>" }
            }
        }
        info { "Article: ${list.size}" }

        return list
    }

    fun getSavedNewsByCategory(category: String): MutableList<Article> {

        val list = mutableListOf<Article>()
        info { "Article: getting db $category" }
        val realmResults = Realm.getDefaultInstance().where(Article::class.java).equalTo("category", category).findAll()

        Realm.getDefaultInstance().executeTransaction { realm ->
            realmResults.forEach {
                list.add(realm.copyFromRealm(it))
            }
        }

        info { "Article: db ${list.size}" }
        return list
    }
}