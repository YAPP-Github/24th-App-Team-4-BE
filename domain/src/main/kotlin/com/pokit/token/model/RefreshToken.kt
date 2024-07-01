package com.pokit.token.model

data class RefreshToken(
    val userId: Long,
    val token: String,
)
