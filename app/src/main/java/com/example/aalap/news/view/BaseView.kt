package com.example.aalap.news.view

import com.example.aalap.news.models.newsmodels.Article

interface BaseView {

    fun loading(isVisible: Boolean)

    fun showError(errorMsg: String?)

    fun displayArticlesR(articles: MutableList<Article>?)
}
