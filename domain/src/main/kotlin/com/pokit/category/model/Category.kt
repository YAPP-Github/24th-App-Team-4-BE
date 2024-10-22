package com.pokit.category.model

import java.time.LocalDateTime

data class Category(
    val categoryId: Long = 0L,
    val userId: Long,
    var categoryName: String,
    var categoryImage: CategoryImage,
    var contentCount: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var openType: OpenType,
) {
    fun update(categoryName: String, categoryImage: CategoryImage) {
        this.categoryName = categoryName
        this.categoryImage = categoryImage
    }

    fun completeShare(): Category {
        this.openType = OpenType.PUBLIC
        return this
    }
}

data class RemindCategory(
    val categoryId: Long,
    val categoryName: String,
)

fun Category.toRemindCategory() = RemindCategory(
    categoryId = this.categoryId,
    categoryName = this.categoryName,
)

fun Category.duplicate(newCategoryName: String, userId: Long, categoryImage: CategoryImage): Category {
    return this.copy(
        categoryId = 0L,
        userId = userId,
        categoryName = newCategoryName,
        categoryImage = categoryImage
    )
}
