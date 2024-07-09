package com.pokit.token.dto.request

data class SignInRequest(
    val authPlatform: String,
    val authorizationCode: String,
)
