package com.pokit.out.persistence.log.persist

import com.pokit.log.model.LogType
import org.springframework.data.jpa.repository.JpaRepository

interface UserLogRepository : JpaRepository<UserLogEntity, Long> {
    fun findByContentIdAndUserId(contentId: Long, userId: Long): UserLogEntity?

    fun findTop10ByUserIdAndTypeOrderByCreatedAtDesc(userId: Long, type: LogType): List<UserLogEntity>
}
