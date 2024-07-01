package com.pokit.out.persistence.auth.impl

import com.pokit.auth.port.out.RefreshTokenRepository
import com.pokit.out.persistence.auth.persist.RefreshTokenJpaEntity
import com.pokit.out.persistence.auth.persist.RefreshTokenJpaRepository
import com.pokit.out.persistence.auth.persist.toDomain
import com.pokit.token.model.RefreshToken
import org.springframework.stereotype.Repository

@Repository
class RefreshTokenAdapter(
    private val refreshTokenJpaRepository: RefreshTokenJpaRepository,
) : RefreshTokenRepository {
    override fun save(refreshToken: RefreshToken): RefreshToken {
        val refreshTokenEntity = RefreshTokenJpaEntity.of(refreshToken)
        val savedToken = refreshTokenJpaRepository.save(refreshTokenEntity)
        return savedToken.toDomain()
    }

    override fun findByUserId(userId: Long): RefreshToken? {
        val refreshToken = refreshTokenJpaRepository.findByUserId(userId)
        return refreshToken?.toDomain()
    }

    override fun deleteById(id: Long) {
        refreshTokenJpaRepository.deleteById(id)
    }
}
