package com.example.aalap.news.network

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface LoginLogoutService {

    @GET
    fun getProfile(): Observable<Response<ResponseBody>>

    @FormUrlEncoded
    @POST
    fun logout(@Url url: String): Observable<Response<ResponseBody>>

}