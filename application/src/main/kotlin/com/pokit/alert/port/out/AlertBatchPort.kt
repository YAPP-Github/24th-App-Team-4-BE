package com.pokit.alert.port.out

import com.pokit.alert.model.AlertBatch
import java.time.LocalDate

interface AlertBatchPort {
    fun loadAllByShouldBeSentAt(now: LocalDate): List<AlertBatch>
}
