package com.pokit.out.persistence.log.persist

import org.springframework.data.jpa.repository.JpaRepository

interface UserLogRepository : JpaRepository<UserLogEntity, Long> {
    fun findByContentIdAndUserId(contentId: Long, userId: Long): UserLogEntity?
}
