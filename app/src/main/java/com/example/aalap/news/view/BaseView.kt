package com.example.aalap.news.view

import com.example.aalap.news.models.newsmodels.Article
//import com.example.aalap.news.models.newsmodels.RArticle

interface BaseView {

    fun loading(isVisible: Boolean)

    fun showError(errorMsg: String?)

    fun displayArticlesR(articles: List<Article>?)
}
