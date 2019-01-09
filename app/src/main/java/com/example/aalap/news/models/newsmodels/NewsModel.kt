package com.example.aalap.news.models.newsmodels

import com.example.aalap.news.newsService
import com.example.aalap.news.pref
import io.reactivex.Observable
import io.reactivex.Single
import io.realm.Realm
import io.realm.RealmResults
import okhttp3.ResponseBody
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response

class NewsModel : AnkoLogger {


    fun getTopHeadlinesByCountry(): Single<List<Article>>? {
        return newsService.getTopHeadlinesByCountry("us")
                .map { it.body()?.articles }
    }

    fun getEverything(category: String, page: Long, pageSize: Int): Single<List<Article>>? {
        return newsService.getEverything(category)
                .map { it.body()?.articles }
    }

    fun getTopHeadlinesByCountryAndCategory(category: String): Observable<List<Article>>? {
        val country = Country.countryKeyValueMap()[pref.getCountry()]
        info { "Country: requesting..$country" }

        return newsService.getTopHeadlines(country!!, category)
                .flatMap { saveToDB(it, category) }
    }


    private fun saveToDB(response: Response<ResponseBody>, category: String): Observable<List<Article>>? {

        val list = mutableListOf<Article>()
        Realm.getDefaultInstance().use {
            it.executeTransaction { realm ->
                val res = response.body()?.string()
                realm.where(News::class.java).findFirst()?.deleteFromRealm()
                realm.createObjectFromJson(News::class.java, res)
                val articles = Realm.getDefaultInstance().where(News::class.java).findFirst()?.articles
                articles?.forEach {
                    it.category = category
                    list.add(realm.copyFromRealm(it))
                    info { "article: $it" }
                }
            }
        }
        info { "Article: ${list.size}" }

        return Observable.just(list)
    }

    fun getDBNews(category: String): MutableList<Article> {

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