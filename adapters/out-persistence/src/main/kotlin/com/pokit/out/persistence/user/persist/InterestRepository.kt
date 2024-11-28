package com.pokit.out.persistence.user.persist

import org.springframework.data.jpa.repository.JpaRepository

interface InterestRepository : JpaRepository<InterestEntity, Long> {
    fun findAllByUserId(userId: Long): List<InterestEntity>
}
