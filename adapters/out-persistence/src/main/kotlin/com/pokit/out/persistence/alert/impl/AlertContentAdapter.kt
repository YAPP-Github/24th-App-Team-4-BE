package com.pokit.out.persistence.alert.impl

import com.pokit.alert.model.AlertContent
import com.pokit.alert.port.out.AlertContentPort
import com.pokit.out.persistence.alert.persist.AlertContentEntity
import com.pokit.out.persistence.alert.persist.AlertContentRepository
import com.pokit.out.persistence.alert.persist.toDomain
import org.springframework.stereotype.Repository

@Repository
class AlertContentAdapter(
    private val alertContentRepository: AlertContentRepository
) : AlertContentPort {
    override fun loadAllInAlertBatchIds(ids: List<Long>): List<AlertContent> {
        return alertContentRepository.findAllByAlertBatchIdIn(ids)
            .map { it.toDomain() }
    }

    override fun deleteAll(ids: List<Long>) {
        alertContentRepository.deleteAllByIdInBatch(ids)
    }

    override fun persist(alertContent: AlertContent): AlertContent {
        val alertContentEntity = AlertContentEntity.of(alertContent)
        return alertContentRepository.save(alertContentEntity).toDomain()
    }
}
