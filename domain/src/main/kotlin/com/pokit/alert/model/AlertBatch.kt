package com.pokit.alert.model

import java.time.LocalDate

data class AlertBatch(
    val userId: Long,
    val shouldBeSentAt: LocalDate,
)
