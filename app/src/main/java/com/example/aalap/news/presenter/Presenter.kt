package com.example.aalap.news.presenter

import com.example.aalap.news.models.NewsModel
import com.example.aalap.news.view.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class Presenter(var view: MainView, var model: NewsModel) {

    var compositeDisposable = CompositeDisposable()

    fun getAllHeadlinesByCountry() {

        model.getTopHeadlinesByCountry()
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribeOn(Schedulers.io())
                ?.subscribe { t1, t2 ->
                    if(t1 != null)
                        view.addNews(t1)
                    else
                        view.showError(t2.localizedMessage)
                }?.let {
                    compositeDisposable.add(it
                    )
                }
    }
}