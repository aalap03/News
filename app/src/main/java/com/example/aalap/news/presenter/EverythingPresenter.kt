package com.example.aalap.news.presenter

import com.example.aalap.news.models.newsmodels.NewsModel
import com.example.aalap.news.newsService
import com.example.aalap.news.view.EverythingView

class EverythingPresenter(var view: EverythingView, var model: NewsModel) {

    fun requestEverythingNews(query: String){
        view.loading(true)
        newsService.getEverything(query)
    }

    fun showArticles() {

    }
}