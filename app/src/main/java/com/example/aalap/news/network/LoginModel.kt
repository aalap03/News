package com.example.aalap.news.network

class LoginModel {

    var retrofit = RetrofitCreator().getService(LoginService::class.java)

    fun requestRegion() {
        retrofit.getRegion()
    }
}