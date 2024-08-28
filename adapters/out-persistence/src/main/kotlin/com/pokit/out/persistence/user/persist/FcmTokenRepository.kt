package com.pokit.out.persistence.user.persist

import org.springframework.data.jpa.repository.JpaRepository

interface FcmTokenRepository : JpaRepository<FcmTokenEntity, Long> {
    fun findByUserId(userId: Long): List<FcmTokenEntity>
}
