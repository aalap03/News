package com.example.aalap.news.network

interface LoginView {

    fun domainSuccess()
    fun domainFailed(errorMsg: String)
}