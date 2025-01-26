package com.pokit.category.model

import com.pokit.category.dto.CategoryCommand
import com.pokit.user.model.InterestType
import java.time.LocalDateTime

data class Category(
    val categoryId: Long = 0L,
    val userId: Long,
    var categoryName: String,
    var categoryImage: CategoryImage,
    var contentCount: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var openType: OpenType,
    var userCount: Int = 0,
    var isShared: Boolean = false,
    var keyword: InterestType,
    var ownerId: Long,
    val isFavorite: Boolean = false,
) {
    fun update(command: CategoryCommand, categoryImage: CategoryImage) {
        this.categoryName = command.categoryName
        this.categoryImage = categoryImage
        this.openType = command.openType
        this.keyword = command.keywordType
    }

    fun completeShare(): Category {
        this.openType = OpenType.PUBLIC
        return this
    }

    fun addUserCount() {
        this.userCount++
    }

    fun minusUserCount() {
        this.userCount--
    }

    fun shared() {
        this.isShared = true
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
