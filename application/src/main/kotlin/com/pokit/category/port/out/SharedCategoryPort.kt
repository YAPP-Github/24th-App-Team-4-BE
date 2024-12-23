package com.pokit.category.port.out

import com.pokit.category.model.SharedCategory

interface SharedCategoryPort {
    fun persist(sharedCategory: SharedCategory): SharedCategory

    fun loadByUserIdAndCategoryId(userId: Long, categoryId: Long): SharedCategory?

    fun delete(sharedCategory: SharedCategory)

    fun loadFirstByCategoryId(categoryId: Long): SharedCategory?

    fun loadByUserId(userId: Long): List<SharedCategory>

    fun loadByCategoryId(categoryId: Long): List<SharedCategory>
}
