package com.pokit.out.persistence.auth.persist

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenJpaEntity, Long> {
    fun findByUserId(userId: Long): RefreshTokenJpaEntity?
}
