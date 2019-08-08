package com.example.aalap.news.network

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

class DomainAndAuthServiceImpl {

    var service: IDomainAndAuthService = RetrofitCreator().getService(IDomainAndAuthService::class.java)

    fun getRegionFromDomain(domain: String): Observable<Response<DomainRes>> {
        return service.getRegion("https://$domain.e-cobalt.com")
    }
}

data class DomainRes(val region: String?, var errorString: String? = null)