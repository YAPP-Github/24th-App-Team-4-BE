package com.pokit.out.persistence.alert.persist

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AlertBatchRepository : JpaRepository<AlertBatchEntity, Long> {
    fun findAllByShouldBeSentAtAfterAndSent(now: LocalDate, send: Boolean, pageable: Pageable): Page<AlertBatchEntity>

    fun findByUserIdAndShouldBeSentAtAndSent(userId: Long, date: LocalDate, sent: Boolean): AlertBatchEntity?
}
