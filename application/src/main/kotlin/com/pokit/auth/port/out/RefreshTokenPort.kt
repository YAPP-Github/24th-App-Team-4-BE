package com.pokit.auth.port.out

import com.pokit.token.model.RefreshToken

interface RefreshTokenPort {
    fun persist(refreshToken: RefreshToken): RefreshToken

    fun loadByUserId(userId: Long): RefreshToken?

    fun deleteById(id: Long)
}
