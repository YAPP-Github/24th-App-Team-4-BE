package com.pokit.alert.port.out

import com.pokit.alert.model.AlertBatch
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface AlertBatchPort {
    fun loadAllByShouldBeSentAt(now: LocalDate, pageable: Pageable): Page<AlertBatch>

    fun send(alertBatch: AlertBatch)
}
