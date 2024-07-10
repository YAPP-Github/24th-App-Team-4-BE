package com.pokit.out.persistence.auth.persist

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenRepository : JpaRepository<RefreshTokenJpaEntity, Long> {
    fun findByUserId(userId: Long): RefreshTokenJpaEntity?
}
