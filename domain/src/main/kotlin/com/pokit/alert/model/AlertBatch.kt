package com.pokit.alert.model

import java.time.LocalDate

data class AlertBatch(
    val id: Long = 0L,
    val userId: Long,
    val shouldBeSentAt: LocalDate,
)
