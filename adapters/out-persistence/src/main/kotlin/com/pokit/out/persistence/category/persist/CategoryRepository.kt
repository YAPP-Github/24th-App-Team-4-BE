package com.pokit.out.persistence.category.persist

import com.pokit.category.model.OpenType
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<CategoryEntity, Long> {
    fun existsByNameAndUserIdAndDeleted(name: String, userId: Long, deleted: Boolean): Boolean
    fun findByUserIdAndDeleted(userId: Long, deleted: Boolean, pageable: Pageable): Slice<CategoryEntity>
    fun findByIdAndUserIdAndDeleted(id: Long, userId: Long, deleted: Boolean): CategoryEntity?
    fun countByUserIdAndDeleted(userId: Long, deleted: Boolean): Int
    fun findByIdAndOpenTypeAndDeleted(id: Long, openType: OpenType, deleted:Boolean): CategoryEntity?
}
