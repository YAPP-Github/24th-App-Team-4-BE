package com.pokit.category.dto.request

import jakarta.validation.constraints.Size

data class DuplicateCategoryRequest (
    val originCategoryId: Long,
    @field:Size(min = 1, max = 10, message = "최대 10자까지 입력 가능합니다.")
    val categoryName: String,
)
