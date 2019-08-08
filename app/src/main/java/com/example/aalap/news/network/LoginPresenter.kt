package com.example.aalap.news.network

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class LoginPresenter(private val view: LoginView, private val model: LoginModel) : AnkoLogger {

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

    fun authenticateAndGetToken(username: String, password: String) {
        model.requestLogin(username, password)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ res ->
                    res?.let {
                        info { "token: ${it.tokenRes?.token}" }
                        info { "chatToken: ${it.tokenRes?.chatToken}" }
                        info { "tokenError: ${it.errorString}" }
                    }
                }, { throwable ->
                    info { "tokenThrowable: ${throwable.localizedMessage}" }

                })?.let {
                    model.compositeDisposable.add(
                            it)
                }
    }


    fun clean() = model.cleanModel()
}