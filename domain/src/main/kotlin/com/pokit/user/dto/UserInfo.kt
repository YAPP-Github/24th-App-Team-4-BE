package com.pokit.user.dto

import com.pokit.token.model.AuthPlatform

data class UserInfo(
    val email: String,
    val authPlatform: AuthPlatform
)
