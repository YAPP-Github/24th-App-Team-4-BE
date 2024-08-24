package com.pokit.category

import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import com.pokit.category.model.CategoryStatus
import com.pokit.category.model.OpenType

class CategoryFixture {
    companion object {
        fun getCategory(userId: Long, image: CategoryImage) = Category(
            userId = userId,
            categoryName = "스포츠/레저",
            categoryImage = image,
            openType = OpenType.PUBLIC
        )

        fun getCategory(userId: Long) = Category(
            categoryId = 1L,
            userId = userId,
            categoryName = "스포츠/레저",
            categoryImage = CategoryImage(1, "www.s3.com"),
            openType = OpenType.PUBLIC
        )

        fun getUnCategorized(userId: Long, image: CategoryImage) = Category(
            userId = userId,
            categoryName = CategoryStatus.UNCATEGORIZED.displayName,
            categoryImage = image,
            openType = OpenType.PUBLIC
        )
    }
}
