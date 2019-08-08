package com.example.aalap.news.network

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface IDomainAndAuthService {

    @GET("https://region.e-cobalt.com")
    fun getRegion(@Header("Origin") url: String): Observable<Response<DomainRes>>

    @FormUrlEncoded
    @POST
    fun authenticate(@Url url: String,
              @Field("username") username: String,
              @Field("password") password: String,
              @Field("deviceId") deviceId: String,
              @Field("userAgent") applicationName: String,
              @Field("userAgentVersion") appVersionCode: String,
              @Field("userOs") userOs: String,
              @Field("userOsVersion") userOsVersion: String): Observable<Response<ResponseBody>>

}