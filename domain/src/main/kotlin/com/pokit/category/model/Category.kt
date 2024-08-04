package com.pokit.category.model

data class Category(
    val categoryId: Long = 0L,
    val userId: Long,
    var categoryName: String,
    var categoryImage: CategoryImage,
    var contentCount: Int = 0,
) {
    fun update(categoryName: String, categoryImage: CategoryImage) {
        this.categoryName = categoryName
        this.categoryImage = categoryImage
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
