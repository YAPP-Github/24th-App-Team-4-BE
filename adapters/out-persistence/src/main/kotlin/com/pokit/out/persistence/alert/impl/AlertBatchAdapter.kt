package com.pokit.out.persistence.alert.impl

import com.pokit.alert.model.AlertBatch
import com.pokit.alert.port.out.AlertBatchPort
import com.pokit.out.persistence.alert.persist.AlertBatchRepository
import com.pokit.out.persistence.alert.persist.toDomain
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class AlertBatchAdapter(
    private val alertBatchRepository: AlertBatchRepository
) : AlertBatchPort {
    override fun loadAllByShouldBeSentAt(now: LocalDate, pageable: Pageable): Page<AlertBatch> {
        return alertBatchRepository.findAllByShouldBeSentAtAfterAndSent(now, false, pageable)
            .map { it.toDomain() }
    }

    override fun send(alertBatch: AlertBatch) {
        alertBatchRepository.findByIdOrNull(alertBatch.id)
            ?.sent()
    }
}
