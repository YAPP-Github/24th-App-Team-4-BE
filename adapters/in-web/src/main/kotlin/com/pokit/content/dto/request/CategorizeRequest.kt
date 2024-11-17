package com.pokit.content.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull

data class CategorizeRequest(
    val contentIds: List<Long>,
    @Schema(description = "이동시키려는 카테고리 ID")
    @NotNull(message = "카테고리 ID는 필수값입니다.")
    val categoryId: Long
)

internal fun CategorizeRequest.toDto() = CategorizeCommand(
    contentIds = this.contentIds,
    categoryId = this.categoryId
)
