package com.pokit.category.dto

import com.pokit.category.model.OpenType
import com.pokit.user.model.InterestType

data class CategoryCommand(
    val categoryName: String,
    val categoryImageId: Int,
    val openType: OpenType = OpenType.PRIVATE,
    val keywordType: InterestType = InterestType.DEFAULT,
)
