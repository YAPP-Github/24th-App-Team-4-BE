package com.pokit.auth.port.out

import com.pokit.user.dto.UserInfo

interface AppleApiClient {
    fun getUserInfo(authorizationCode: String): UserInfo
}
