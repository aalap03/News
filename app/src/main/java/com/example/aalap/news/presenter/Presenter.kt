package com.example.aalap.news.presenter

import android.util.Log
import com.example.aalap.news.models.Article
import com.example.aalap.news.models.NewsModel
import com.example.aalap.news.view.MainView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiConsumer
import io.reactivex.schedulers.Schedulers

class Presenter(var view: MainView, var model: NewsModel) {

    private var compositeDisposable = CompositeDisposable()
    private val TAG = "Presenter: "

    fun getAllHeadlinesByCountry() {

        view.loading(true)
        getAndDisplayList(model.getTopHeadlinesByCountry())
    }

    fun getAllHeadlinesByCategory(category: String) {

        view.loading(true)
        getAndDisplayList(model.getTopHeadlinesByCategory(category))
    }

    fun getEverythingArticle(query: String?) {
        getAndDisplayList(query?.let { model.getEverything(it) })
    }

    //this receives list single as parameter, subscribes it and display it on the view
    private fun getAndDisplayList(single: Single<List<Article>>?) {
        single?.subscribeOn(Schedulers.io())
//                ?.onErrorResumeNext(single) //Give db items at this point
                ?.doOnError { Log.d(TAG, it.localizedMessage) }
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(getListConsumer())
                ?.let { compositeDisposable.add(it) }
    }

    private fun getListConsumer(): BiConsumer<List<Article>, Throwable> {
        return BiConsumer { articles, throwable ->
            if (articles != null)
                view.addNews(articles)
            else
                view.showError(throwable?.localizedMessage)
        }
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}