package com.pokit.out.persistence.alert.impl

import com.pokit.alert.model.AlertContent
import com.pokit.alert.port.out.AlertContentPort
import com.pokit.out.persistence.alert.persist.AlertContentRepository
import com.pokit.out.persistence.alert.persist.toDomain
import org.springframework.stereotype.Repository

@Repository
class AlertContentAdapter(
    private val alertContentRepository: AlertContentRepository
) : AlertContentPort {
    override fun loadAllInAlertBatchIds(ids: List<Long>): List<AlertContent> {
        return alertContentRepository.findAllByAlertBatchIdInAndDeleted(ids, false)
            .map { it.toDomain() }
    }

    override fun deleteAll(ids: List<Long>) {
        alertContentRepository.deleteAllInIds(ids)
    }
}
