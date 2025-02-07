package com.pokit.category.v1.dto.request

import com.pokit.category.dto.CategoryCommand
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCategoryRequest(
    @field:NotBlank(message = "포킷 명은 필수 값입니다.")
    @field:Size(min = 1, max = 10, message = "최대 10자까지 입력 가능합니다.")
    val categoryName: String,
    val categoryImageId: Int,
)

internal fun CreateCategoryRequest.toDto() = CategoryCommand(
    categoryName = this.categoryName,
    categoryImageId = this.categoryImageId
)
