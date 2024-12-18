package com.pokit.out.persistence.category.persist

import org.springframework.data.jpa.repository.JpaRepository

interface SharedCategoryRepository : JpaRepository<SharedCategoryEntity, Long> {
    fun findByUserIdAndCategoryIdAndIsDeleted(
        userId: Long,
        categoryId: Long,
        isDeleted: Boolean
    ): SharedCategoryEntity?

    fun findFirstByCategoryIdAndIsDeletedOrderByCreatedAt(
        categoryId: Long,
        deleted: Boolean
    ): SharedCategoryEntity?
}
