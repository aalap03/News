package com.example.aalap.news.network.serviceImpl

import android.os.Build
import com.example.aalap.news.network.RetrofitCreator
import com.example.aalap.news.network.datamodel.AuthRes
import com.example.aalap.news.network.datamodel.DomainRes
import com.example.aalap.news.network.datamodel.TokenRes
import com.example.aalap.news.network.service.IAuthService
import com.example.aalap.news.network.service.IRegionService
import io.reactivex.Observable
import retrofit2.Response
import java.util.*

class LoginLogoutServicesImpl {

    var serviceRegion: IRegionService = RetrofitCreator().getDomainServiceWithoutOrigin()
    var serviceAuth: IAuthService = RetrofitCreator().getService(IAuthService::class.java)

    fun getRegionFromDomain(domain: String): Observable<Response<DomainRes>> {
        return serviceRegion.getRegion("https://$domain.e-cobalt.com")
    }

    fun authenticateAndGetToken(username: String, password: String): Observable<AuthRes?>? {

        return serviceAuth.authenticate("https://auth-ca1.e-cobalt.com/authenticate",
                username, password,
                UUID.randomUUID().toString(),
                "Cobalt_Android",
                "abc",
                "Android",
                Build.VERSION.RELEASE)
                .map { getAndSaveTokenRes(it) }
                .onErrorReturn { AuthRes(null, it.localizedMessage) }
    }

    private fun getAndSaveTokenRes(it: Response<TokenRes>): AuthRes? {

        return if (it.isSuccessful) {
            it.body()?.let {
                AuthRes(it)
            }
        } else {
            it.errorBody()?.let {
                AuthRes(null, it.string())
            }
        }
    }
}