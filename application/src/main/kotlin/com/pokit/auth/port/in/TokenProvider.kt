package com.pokit.auth.port.`in`

import com.pokit.token.model.Token

interface TokenProvider {
    fun createToken(userId: Long): Token

    fun reissueToken(userId: Long, refreshToken: String): String

    fun deleteRefreshToken(refreshTokenId: Long)

    fun getUserId(token: String): Long
}
