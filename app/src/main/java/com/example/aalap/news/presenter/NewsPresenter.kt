package com.example.aalap.news.presenter

import com.example.aalap.news.models.newsmodels.*
import com.example.aalap.news.view.NewsListView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.net.UnknownHostException

class NewsPresenter(var view: NewsListView, private var model: NewsModel) : AnkoLogger {

    private var compositeDisposable = CompositeDisposable()

    fun getEverythingNews(query: String) {

        model.getSearchedHeadlines(query)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { newsResult, throwable ->

                    when (newsResult) {
                        is NewsResult.NewsData -> view.displayArticlesR(newsResult.list)
                        is NewsResult.NewsError -> view.showError(newsResult.error)
                    }
                    throwable?.printStackTrace()
                    view.showError(throwable?.localizedMessage)

                }?.let { compositeDisposable.add(it) }
    }

    fun requestHeadlinesByCountryAndCategory(category: String) {

        view.loading(true)
        model.getHeadlinesWithCountryAndCategory(category)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { newsResult ->
                            when (newsResult) {
                                is NewsResult.NewsData -> view.displayArticlesR(newsResult.list)
                                is NewsResult.NewsError -> {
                                    view.showError(newsResult.error)
                                    view.displayArticlesR(newsResult.dbList)
                                }
                            }
                        },
                        { t ->
                            view.showError(t.localizedMessage)
                            view.displayArticlesR(model.getSavedNewsByCategory(category))
                        })
                .let { compositeDisposable.add(it) }
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}