package com.pokit.category.v2.dto.request

import com.pokit.category.dto.CategoryCommand
import com.pokit.category.model.OpenType
import com.pokit.user.model.InterestType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCategoryRequestV2(
    @field:NotBlank(message = "포킷 명은 필수 값입니다.")
    @field:Size(min = 1, max = 10, message = "최대 10자까지 입력 가능합니다.")
    val categoryName: String,
    val categoryImageId: Int,
    val openType: String,
    val keywordType: String,
)

internal fun CreateCategoryRequestV2.toDto() = CategoryCommand(
    categoryName = this.categoryName,
    categoryImageId = this.categoryImageId,
    openType = OpenType.of(this.openType),
    keywordType = InterestType.of(this.keywordType)
)
