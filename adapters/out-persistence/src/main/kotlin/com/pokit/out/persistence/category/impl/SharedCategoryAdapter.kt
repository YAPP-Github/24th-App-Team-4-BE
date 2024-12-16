package com.pokit.out.persistence.category.impl

import com.pokit.category.model.SharedCategory
import com.pokit.category.port.out.SharedCategoryPort
import com.pokit.out.persistence.category.persist.SharedCategoryEntity
import com.pokit.out.persistence.category.persist.SharedCategoryRepository
import com.pokit.out.persistence.category.persist.toDomain
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class SharedCategoryAdapter(
    private val sharedCategoryRepository: SharedCategoryRepository
) : SharedCategoryPort {
    override fun persist(sharedCategory: SharedCategory): SharedCategory {
        return sharedCategoryRepository.save(SharedCategoryEntity.of(sharedCategory))
            .toDomain()
    }

    override fun loadByUserIdAndCategoryId(userId: Long, categoryId: Long): SharedCategory? {
        return sharedCategoryRepository.findByUserIdAndCategoryIdAndIsDeleted(
            userId,
            categoryId,
            false
        )?.toDomain()
    }

    override fun delete(sharedCategory: SharedCategory) {
        sharedCategoryRepository.findByIdOrNull(sharedCategory.id)
            ?.delete()
    }

    override fun loadFirstByCategoryId(categoryId: Long): SharedCategory? {
        return sharedCategoryRepository.findFirstByCategoryIdOrderByCreatedAt(categoryId)
            ?.toDomain()
    }
}
