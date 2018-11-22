package com.example.aalap.news.presenter

import com.example.aalap.news.models.newsmodels.Article
import com.example.aalap.news.models.newsmodels.NewsModel
import com.example.aalap.news.view.NewsListView
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

    fun getAllHeadlinesByCountry() {
        view.loading(true)
        getAndDisplayList(model.getTopHeadlinesByCountry())
    }

    fun getAllHeadlinesByCategory(category: String) {
        view.loading(true)

        model.getTopHeadlinesByCountryAndCategory(category)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ list ->
                    view.displayArticlesR(list)
                }, { throwable ->
                    if (throwable is UnknownHostException) {
                        view.showError("No internet")
                        model.getDBNews(category).let { view.displayArticlesR(it) }
                    }
                    else
                        view.showError(throwable.localizedMessage)
                })?.let {
                    compositeDisposable.add(it)
                }

    }

    fun getEverythingArticle(query: String?, page: Long, pageSize: Int) {
        view.loading(true)
        getAndDisplayList(query?.let { model.getEverything(it, page, pageSize) })
    }

    //this receives list single as parameter, subscribes it and display it on the view
    private fun getAndDisplayList(single: Single<List<Article>>?) {
        if (single != null)
            single.subscribeOn(Schedulers.io())
                    .doOnError { info { it.localizedMessage } }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(getListConsumer())
                    .let { compositeDisposable.add(it) }
        else
            view.showError("Feed Not found")
    }

    private fun getListConsumer(): BiConsumer<List<Article>, Throwable> {
        return BiConsumer { articles, throwable ->
            info { "Articles: ${articles?.size}" }
            when {
                articles != null -> view.displayArticlesR(articles)
                throwable != null -> view.showError(throwable.localizedMessage)
                else -> view.showError("Are bhai kehna kya chahte ho???")
            }
        }
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}