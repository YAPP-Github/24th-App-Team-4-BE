package com.pokit.category.v2.dto.request

import com.pokit.category.dto.DuplicateCategoryCommandV2
import com.pokit.category.model.OpenType
import com.pokit.user.model.InterestType
import jakarta.validation.constraints.Size

data class DuplicateCategoryRequestV2(
    val originCategoryId: Long,
    @field:Size(min = 1, max = 10, message = "최대 10자까지 입력 가능합니다.")
    val categoryName: String,
    val categoryImageId: Int,
    val keyword: String,
    val openType: String,
)

internal fun DuplicateCategoryRequestV2.toDto() = DuplicateCategoryCommandV2(
    originCategoryId = originCategoryId,
    categoryName = categoryName,
    categoryImageId = categoryImageId,
    keyword = InterestType.of(keyword),
    openType = OpenType.of(openType),
)
