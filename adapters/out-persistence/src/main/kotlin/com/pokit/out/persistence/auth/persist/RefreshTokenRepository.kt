package com.pokit.out.persistence.auth.persist

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {
    fun findByUserId(userId: Long): RefreshTokenEntity?
}
