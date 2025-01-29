package com.pokit.category.dto

import com.pokit.category.model.OpenType
import com.pokit.user.model.InterestType

data class DuplicateCategoryCommandV2(
    val originCategoryId: Long,
    val categoryName: String,
    val categoryImageId: Int,
    val keyword: InterestType,
    val openType: OpenType,
)
