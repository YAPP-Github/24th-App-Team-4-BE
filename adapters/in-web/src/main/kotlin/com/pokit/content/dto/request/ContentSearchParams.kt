package com.pokit.content.dto.request

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class ContentSearchParams(
    val categoryId: Long?,
    val isRead: Boolean?,
    val favorites: Boolean?,
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    val startDate: LocalDate?,
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    val endDate: LocalDate?,
    val categoryIds: List<Long>?,
    val searchWord: String?
)

internal fun ContentSearchParams.toDto() = ContentSearchCondition(
    categoryId = this.categoryId,
    isRead = this.isRead,
    favorites = this.favorites,
    startDate = this.startDate,
    endDate = this.endDate,
    categoryIds = this.categoryIds,
    searchWord = this.searchWord
)
