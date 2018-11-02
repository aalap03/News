package com.example.aalap.news.models.newsmodels

import com.google.gson.annotations.SerializedName

data class Source(@SerializedName("id") var id: String,
                  @SerializedName("name") var name: String
)

//{
//    -"source": {
//    "id": null,
//    "name": "Yahoo.com"
//},
//    "author": null,
//    "title": "World Series Game 4: Five things you need to know before first pitch",
//    "description": null,
//    "url": "https://sports.yahoo.com/world-series-game-4-five-things-need-know-first-pitch-195933031.html",
//    "urlToImage": null,
//    "publishedAt": "2018-10-27T21:02:31Z",
//    "content": null
//}

data class Article(@SerializedName("source") var source: Source,
                   @SerializedName("author") var author: String,
                   @SerializedName("title") var title: String,
                   @SerializedName("description") var description: String,
                   @SerializedName("url") var url: String,
                   @SerializedName("urlToImage") var urlToImage: String,
                   @SerializedName("publishedAt") var publishedAt: String
)

data class News(@SerializedName("status") var status: String,
                @SerializedName("totalResults") var totalResults: Int,
                @SerializedName("articles") var articles: List<Article>
)