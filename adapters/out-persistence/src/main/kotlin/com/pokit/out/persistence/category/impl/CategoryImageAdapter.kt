package com.pokit.out.persistence.category.impl

import com.pokit.category.model.CategoryImage
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.out.persistence.category.persist.CategoryImageRepository
import com.pokit.out.persistence.category.persist.toDomain
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component

@Component
class CategoryImageAdapter(
    private val categoryImageRepository: CategoryImageRepository
) : CategoryImagePort {
    override fun loadById(id: Int): CategoryImage? =
        categoryImageRepository.findByIdOrNull(id)?.toDomain()

    override fun loadAll(): List<CategoryImage> =
        categoryImageRepository.findAll()
            .map { it.toDomain() }
}
