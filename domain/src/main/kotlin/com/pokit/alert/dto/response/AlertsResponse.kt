package com.pokit.alert.dto.response

import com.pokit.alert.model.Alert
import com.pokit.alert.model.AlertDefault

data class AlertsResponse(
    val id: Long,
    val userId: Long,
    val contentId: Long,
    val thumbNail: String,
    val title: String,
    val body: String = AlertDefault.body,
    val createdAt: String
)

fun Alert.toAlertsResponse(dayDifference: Int) = AlertsResponse(
    id = this.id,
    userId = this.userId,
    contentId = this.id,
    thumbNail = this.contentThumbNail,
    title = this.title,
    createdAt = if (dayDifference == 0) "오늘" else "${dayDifference}일 전"
)
