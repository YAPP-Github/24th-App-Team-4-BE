package com.pokit.user.dto

import com.pokit.token.model.AuthPlatform

data class UserInfo(
    var email: String?,
    val authPlatform: AuthPlatform,
    val sub: String
)
