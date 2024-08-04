package com.pokit.auth.port.out

import com.pokit.user.dto.UserInfo

interface AppleApiClient {
    fun getUserInfo(idToken: String): UserInfo

    fun revoke(refreshToken: String)
}
