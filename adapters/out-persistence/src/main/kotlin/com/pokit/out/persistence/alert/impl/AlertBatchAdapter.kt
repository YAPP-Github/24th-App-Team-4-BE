package com.pokit.out.persistence.alert.impl

import com.pokit.alert.model.AlertBatch
import com.pokit.alert.port.out.AlertBatchPort
import com.pokit.out.persistence.alert.persist.AlertBatchRepository
import com.pokit.out.persistence.alert.persist.toDomain
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class AlertBatchAdapter(
    private val alertBatchRepository: AlertBatchRepository
) : AlertBatchPort{
    override fun loadAllByShouldBeSentAt(now: LocalDate): List<AlertBatch> {
        return alertBatchRepository.findAllByShouldBeSentAtAfterAndSent(now, false)
            .map { it.toDomain() }
    }
}
