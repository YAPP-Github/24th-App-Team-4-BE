package com.pokit.category.port.service

import com.pokit.category.dto.CategoryCommand
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import com.pokit.category.model.CategoryStatus.UNCATEGORIZED
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.AlreadyExistsException
import com.pokit.common.exception.InvalidRequestException
import com.pokit.common.exception.NotFoundCustomException
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryService(
    private val categoryPort: CategoryPort,
    private val categoryImagePort: CategoryImagePort
) : CategoryUseCase {
    companion object {
        private const val MAX_CATEGORY_COUNT = 30
    }

    @Transactional
    override fun create(command: CategoryCommand, userId: Long): Category {
        if (command.categoryName == UNCATEGORIZED.displayName) {
            throw InvalidRequestException(CategoryErrorCode.UNAVAILABLE_CATEGORY_NAME)
        }

        if (categoryPort.existsByNameAndUserId(command.categoryName, userId)) {
            throw AlreadyExistsException(CategoryErrorCode.ALREADY_EXISTS_CATEGORY)
        }

        if (categoryPort.countByUserId(userId) >= MAX_CATEGORY_COUNT) {
            throw InvalidRequestException(CategoryErrorCode.MAX_CATEGORY_LIMIT_EXCEEDED)
        }

        val categoryImage = categoryImagePort.loadById(command.categoryImageId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY_IMAGE)

        return categoryPort.persist(
            Category(
                categoryName = command.categoryName,
                categoryImage = categoryImage,
                userId = userId,
            )
        )
    }

    @Transactional
    override fun update(categoryCommand: CategoryCommand, userId: Long, categoryId: Long): Category {
        val category = categoryPort.loadByIdAndUserId(categoryId, userId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)

        val categoryImage = categoryImagePort.loadById(categoryCommand.categoryImageId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY_IMAGE)

        category.update(categoryCommand.categoryName, categoryImage)
        return categoryPort.persist(category)
    }

    @Transactional
    override fun delete(categoryId: Long, userId: Long) {
        val category = categoryPort.loadByIdAndUserId(categoryId, userId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)

        categoryPort.delete(category)
    }

    override fun getTotalCount(userId: Long): Int =
        categoryPort.countByUserId(userId)

    override fun getCategories(userId: Long, pageable: Pageable, filterUncategorized: Boolean): Slice<Category> {
        val categories = categoryPort.loadAllByUserId(userId, pageable)

        val filteredCategories = if (filterUncategorized) {
            categories.content.filter { it.categoryName != UNCATEGORIZED.displayName }
        } else {
            categories.content
        }

        return SliceImpl(filteredCategories, pageable, categories.hasNext())
    }


    override fun getAllCategoryImages(): List<CategoryImage> =
        categoryImagePort.loadAll()

}
