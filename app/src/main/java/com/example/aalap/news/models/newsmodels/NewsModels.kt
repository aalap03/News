package com.example.aalap.news.models.newsmodels

import com.google.gson.annotations.SerializedName
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

//{
//"status": "ok",
//"totalResults": 20,
//-"articles": [
// {
//    -"source": {
//          "id": null,
//          "name": "Yahoo.com"
//     },
//     "author": null,
//     "title": "World Series Game 4: Five things you need to know before first pitch",
//     "description": null,
//     "url": "https://sports.yahoo.com/world-series-game-4-five-things-need-know-first-pitch-195933031.html",
//     "urlToImage": null,
//     "publishedAt": "2018-10-27T21:02:31Z",
//     "content": null
//}]
//}

open class Source(var id: String? = null,
                  var name: String = "") : RealmObject(){
    override fun toString(): String {
        return "Source(id=$id, name='$name')"
    }
}

open class Article(var source: Source? = null,
                   var author: String? = null,
                   var title: String = "",
                   var description: String? = null,
                   var url: String = "",
                   var urlToImage: String? = null,
                   var publishedAt: String = "",
                   var isSaved: Boolean = false,
                   var category: String = "") : RealmObject(){
    override fun toString(): String {
        return "Article(source=$source, author=$author, title='$title', description=$description, url='$url', urlToImage=$urlToImage, publishedAt='$publishedAt', isSaved=$isSaved, category='$category')"
    }
}

open class News(var status: String = "",
                var totalResults: Int = 0,
                var articles: RealmList<Article> = RealmList()) : RealmObject()

sealed class NewsResult {

    data class NewsData(var list: MutableList<Article>) : NewsResult()
    data class NewsError(var error: String, var dbList: MutableList<Article>) : NewsResult()
}