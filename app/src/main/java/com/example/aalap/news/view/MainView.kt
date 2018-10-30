package com.example.aalap.news.view

import com.example.aalap.news.models.Article

interface MainView {

    fun loading(isVisible: Boolean)

    fun showError(errorMsg: String?)

    fun addNews(articles: List<Article>)

}