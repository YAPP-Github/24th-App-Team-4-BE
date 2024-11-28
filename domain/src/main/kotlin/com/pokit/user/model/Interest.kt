package com.pokit.user.model

data class Interest(
    val id: Long = 0L,
    val userId: Long,
    var interestType: InterestType
)
