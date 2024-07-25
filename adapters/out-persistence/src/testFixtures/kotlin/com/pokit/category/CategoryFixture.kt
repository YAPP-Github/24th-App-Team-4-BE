package com.pokit.category

import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage

class CategoryFixture {
    companion object {
        fun getCategory(userId: Long, image: CategoryImage) = Category(
            userId = userId,
            categoryName = "스포츠/레저",
            categoryImage = image
        )

        fun getCategory(userId: Long) = Category(
            userId = userId,
            categoryName = "스포츠/레저",
            categoryImage = CategoryImage(1, "www.s3.com")
        )
    }
}