package com.pokit.category.port.`in`

import com.pokit.category.dto.CategoriesResponse
import com.pokit.category.dto.CategoryCommand
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CategoryUseCase {
    fun create(categoryCommand: CategoryCommand, userId: Long): Category
    fun update(categoryCommand: CategoryCommand, userId: Long, categoryId: Long): Category
    fun delete(categoryId: Long, userId: Long)
    fun getTotalCount(userId: Long): Int
    fun getAllCategoryImages(): List<CategoryImage>
    fun getCategories(userId: Long, pageable: Pageable, filterUncategorized: Boolean): Slice<CategoriesResponse>
    fun getCategory(userId: Long, categoryId: Long): Category
    fun getSharedCategory(categoryId: Long, userId: Long): Category
    fun completeShare(categoryId: Long, userId: Long)
}
