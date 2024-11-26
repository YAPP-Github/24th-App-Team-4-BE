package com.pokit.category.port.out

import com.pokit.category.model.SharedCategory

interface SharedCategoryPort {
    fun persist(sharedCategory: SharedCategory): SharedCategory

    fun loadByUserIdAndCategoryId(userId: Long, categoryId: Long): SharedCategory?
}
