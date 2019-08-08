package com.example.aalap.news.network.datamodel

data class DomainRes(val region: String?, var errorString: String? = null)

data class TokenRes(val token: String, val chatToken: String?)

data class AuthRes(val tokenRes: TokenRes?, val errorString: String? = null)