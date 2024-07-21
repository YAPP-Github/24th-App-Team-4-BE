package com.pokit.category.port.out

import com.pokit.category.model.Category
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CategoryPort {
    fun loadAllByUserId(userId: Long, pageable: Pageable): Slice<Category>
    fun loadByIdAndUserId(id: Long, userId: Long): Category?
    fun existsByNameAndUserId(name: String, userId: Long): Boolean
    fun persist(category: Category): Category
    fun delete(category: Category)
    fun countByUserId(userId: Long): Int
}
