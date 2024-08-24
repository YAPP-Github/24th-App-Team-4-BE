package com.pokit.out.persistence.alert.persist

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface AlertBatchRepository : JpaRepository<AlertBatchEntity, Long> {
    fun findAllByShouldBeSentAtAfterAndSent(now: LocalDate, send: Boolean): List<AlertBatchEntity>
}
