package com.example.aalap.news.network.service

import com.example.aalap.news.network.datamodel.TokenRes
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Url

interface IAuthService {
    @FormUrlEncoded
    @POST
    fun authenticate(@Url url: String,
                     @Field("username") username: String,
                     @Field("password") password: String,
                     @Field("deviceId") deviceId: String,
                     @Field("userAgent") applicationName: String,
                     @Field("userAgentVersion") appVersionCode: String,
                     @Field("userOs") userOs: String,
                     @Field("userOsVersion") userOsVersion: String): Observable<Response<TokenRes>>
}