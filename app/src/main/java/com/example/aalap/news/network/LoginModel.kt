package com.example.aalap.news.network

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import retrofit2.Response

class LoginModel: AnkoLogger {

    var retrofit = DomainAndAuthServiceImpl()
    var compositeDisposable = CompositeDisposable()

    fun requestRegion(domain: String): Observable<DomainRes> {
        return retrofit.getRegionFromDomain(domain)
                .map { getDomainResFromResponse(it) }
                .onErrorReturn { t -> DomainRes(null, t.localizedMessage) }
    }

    private fun getDomainResFromResponse(res: Response<DomainRes>): DomainRes {
        val body = res.body()
        info { "res: $body" }

        return if (res.isSuccessful)
            DomainRes(body?.region)
        else
            DomainRes(null, responseNotSuccessful(res))
    }

    private fun <T> responseNotSuccessful(result: Response<T>): String {
        return when (result.code()) {
            400 -> "Bad request"
            401 or 403 -> "Invalid creds"
            422 -> "invalid domain"
            500 or 502 or 521 -> "server down"
            else -> result.errorBody()?.string().toString()
        }
    }

    fun cleanModel() = compositeDisposable.clear()
}