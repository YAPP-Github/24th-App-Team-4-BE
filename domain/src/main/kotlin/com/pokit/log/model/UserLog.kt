package com.pokit.log.model

data class UserLog(
    val contentId: Long,
    val userId: Long,
    val type: LogType
)
