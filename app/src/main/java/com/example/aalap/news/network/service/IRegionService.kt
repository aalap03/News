package com.example.aalap.news.network.service

import com.example.aalap.news.network.datamodel.DomainRes
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface IRegionService {
    @GET("https://region.e-cobalt.com")
    fun getRegion(@Header("Origin") url: String): Observable<Response<DomainRes>>
}

