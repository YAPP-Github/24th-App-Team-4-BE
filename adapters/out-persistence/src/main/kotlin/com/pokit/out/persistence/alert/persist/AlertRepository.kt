package com.pokit.out.persistence.alert.persist

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface AlertRepository : JpaRepository<AlertEntity, Long> {
    fun findAllByUserIdAndDeleted(userId: Long, deleted: Boolean, pageable: Pageable): Slice<AlertEntity>

    fun findByIdAndUserId(id: Long, userId: Long): AlertEntity?
}
