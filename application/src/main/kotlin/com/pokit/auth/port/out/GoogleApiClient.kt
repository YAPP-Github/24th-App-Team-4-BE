package com.pokit.auth.port.out

import com.pokit.user.dto.UserInfo

interface GoogleApiClient {
    fun getUserInfo(authorizationCode: String): UserInfo
}
