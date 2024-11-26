package com.pokit.out.persistence.category.persist

import org.springframework.data.jpa.repository.JpaRepository

interface SharedCategoryRepository : JpaRepository<SharedCategoryEntity, Long> {
    fun findByUserIdAndCategoryId(userId: Long, categoryId: Long): SharedCategoryEntity?
}
