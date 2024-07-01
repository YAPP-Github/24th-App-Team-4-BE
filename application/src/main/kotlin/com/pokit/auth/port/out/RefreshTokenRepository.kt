package com.pokit.auth.port.out

import com.pokit.token.model.RefreshToken

interface RefreshTokenRepository {
    fun save(refreshToken: RefreshToken): RefreshToken

    fun findByUserId(userId: Long): RefreshToken?

    fun deleteById(id: Long)
}
