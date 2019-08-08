package com.example.aalap.news.network

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginPresenter(private val view: LoginView, private val model: LoginModel) {

    fun requestRegionFromDomain(domain: String) {
        model.compositeDisposable.add(
                model.requestRegion(domain)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ t ->
                            t.region?.let { view.domainSuccess() }
                            t.errorString?.let { view.domainFailed(it) }
                        }, { t ->
                            view.domainFailed(t.localizedMessage)
                        }))
    }

    fun clean() = model.cleanModel()
}