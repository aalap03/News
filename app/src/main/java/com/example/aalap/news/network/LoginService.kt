package com.example.aalap.news.network

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface LoginService {

    @GET("")
    fun getRegion(): Single<Response<ResponseBody>>

    @FormUrlEncoded
    @POST
    fun login(@Url url: String,
                     @Field("username") username: String,
                     @Field("password") password: String,
                     @Field("deviceId") deviceId: String,
                     @Field("userAgent") applicationName: String,
                     @Field("userAgentVersion") appVersionCode: String,
                     @Field("userOs") userOs: String,
                     @Field("userOsVersion") userOsVersion: String): Observable<Response<ResponseBody>>

    @GET
    fun getProfile(@Url url: String): Observable<Response<ResponseBody>>

    @GET
    fun logout(@Url url: String): Observable<Response<ResponseBody>>

}