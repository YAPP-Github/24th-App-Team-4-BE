package com.pokit.token.model

data class Token(
    val accessToken: String,
    val refreshToken: String,
    val isRegistered: Boolean = false
)
