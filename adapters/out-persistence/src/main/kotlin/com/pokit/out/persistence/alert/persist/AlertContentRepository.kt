package com.pokit.out.persistence.alert.persist

import org.springframework.data.jpa.repository.JpaRepository

interface AlertContentRepository : JpaRepository<AlertContentEntity, Long> {
    fun findAllByAlertBatchIdIn(ids: List<Long>): List<AlertContentEntity>
}
