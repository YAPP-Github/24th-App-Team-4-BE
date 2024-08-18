package com.pokit.out.persistence.category.impl

import com.pokit.category.model.Category
import com.pokit.category.model.OpenType
import com.pokit.category.port.out.CategoryPort
import com.pokit.out.persistence.category.persist.CategoryEntity
import com.pokit.out.persistence.category.persist.CategoryRepository
import com.pokit.out.persistence.category.persist.toDomain
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CategoryAdapter(
    private val categoryRepository: CategoryRepository
) : CategoryPort {
    override fun loadAllByUserId(userId: Long, pageable: Pageable): Slice<Category> =
        categoryRepository.findByUserIdAndDeleted(userId, false, pageable)
            .map { it.toDomain() }

    override fun loadByIdAndUserId(id: Long, userId: Long): Category? =
        categoryRepository.findByIdAndUserIdAndDeleted(id, userId, false)?.toDomain()

    override fun loadById(id: Long): Category? =
        categoryRepository.findByIdOrNull(id)?.toDomain()

    override fun existsByNameAndUserId(name: String, userId: Long): Boolean =
        categoryRepository.existsByNameAndUserIdAndDeleted(name, userId, false)

    override fun persist(category: Category): Category {
        val categoryEntity = CategoryEntity.of(category)
        return categoryRepository.save(categoryEntity).toDomain()
    }

    override fun delete(category: Category) {
        categoryRepository.findByIdOrNull(category.categoryId)
            ?.delete()
    }

    override fun countByUserId(userId: Long): Int =
        categoryRepository.countByUserIdAndDeleted(userId, false)

    override fun loadByIdAndOpenType(id: Long, openType: OpenType): Category? =
        categoryRepository.findByIdAndOpenTypeAndDeleted(id, openType, false)?.toDomain()

}
