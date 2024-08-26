package com.pokit.out.persistence.alert.persist

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface AlertContentRepository : JpaRepository<AlertContentEntity, Long> {
    fun findAllByAlertBatchIdInAndDeleted(ids: List<Long>, deleted: Boolean): List<AlertContentEntity>

    @Modifying(clearAutomatically = true)
    @Query(
        """
        update AlertContentEntity ac set ac.deleted = true
        where ac.id in :ids
        """
    )
    fun deleteAllInIds(ids: List<Long>)
}
