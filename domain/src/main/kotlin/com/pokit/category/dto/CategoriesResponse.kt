package com.pokit.category.dto

import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import java.time.format.DateTimeFormatter

data class CategoriesResponse(
    val categoryId: Long,
    val userId: Long,
    var categoryName: String,
    var categoryImage: CategoryImage,
    var contentCount: Int,
    val createdAt: String
)

fun Category.toCategoriesResponse(): CategoriesResponse {
    val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

    return CategoriesResponse(
        categoryId = this.categoryId,
        userId = this.userId,
        categoryName = this.categoryName,
        categoryImage = this.categoryImage,
        contentCount = this.contentCount,
        createdAt = this.createdAt.format(formatter)
    )
}
