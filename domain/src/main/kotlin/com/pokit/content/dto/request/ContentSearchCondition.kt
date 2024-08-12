package com.pokit.content.dto.request

import java.time.LocalDate

data class ContentSearchCondition(
    val categoryId: Long?,
    val isRead: Boolean?,
    val favorites: Boolean?,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val categoryIds: List<Long>?,
    val searchWord: String?
)
