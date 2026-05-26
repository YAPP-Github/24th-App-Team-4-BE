package com.pokit.category.v1.dto.response

import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage

data class CategoryResponse(
    val categoryId: Long,
    var categoryName: String,
    var categoryImage: CategoryImage,
    val alertEnabled: Boolean = false,
)

data class CategoryCountResponse(
    val categoryTotalCount: Int,
)

fun Category.toResponse(alertEnabled: Boolean = false): CategoryResponse = CategoryResponse(
    categoryId = this.categoryId,
    categoryName = this.categoryName,
    categoryImage = this.categoryImage,
    alertEnabled = alertEnabled,
)
