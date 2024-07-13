package com.pokit.out.persistence.auth.impl

import com.pokit.auth.port.out.RefreshTokenPort
import com.pokit.out.persistence.auth.persist.RefreshTokenEntity
import com.pokit.out.persistence.auth.persist.RefreshTokenRepository
import com.pokit.out.persistence.auth.persist.toDomain
import com.pokit.token.model.RefreshToken
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenAdapter(
    private val refreshTokenRepository: RefreshTokenRepository,
) : RefreshTokenPort {
    override fun persist(refreshToken: RefreshToken): RefreshToken {
        val refreshTokenEntity = RefreshTokenEntity.of(refreshToken)
        val savedToken = refreshTokenRepository.save(refreshTokenEntity)
        return savedToken.toDomain()
    }

    override fun loadByUserId(userId: Long): RefreshToken? {
        val refreshToken = refreshTokenRepository.findByUserId(userId)
        return refreshToken?.toDomain()
    }

    override fun deleteById(id: Long) {
        refreshTokenRepository.deleteById(id)
    }
}
