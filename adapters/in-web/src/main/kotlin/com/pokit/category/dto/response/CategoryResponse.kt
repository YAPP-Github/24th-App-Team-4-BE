package com.pokit.category.dto.response

import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage

data class CategoryResponse(
    val categoryId: Long,
    var categoryName: String,
    var categoryImage: CategoryImage,
)

data class CategoryCountResponse(
    val categoryTotalCount: Int,
)

fun Category.toResponse(): CategoryResponse = CategoryResponse(
    categoryId = this.categoryId,
    categoryName = this.categoryName,
    categoryImage = this.categoryImage,
)
