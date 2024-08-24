package com.pokit.category.port.service

import com.pokit.category.dto.CategoriesResponse
import com.pokit.category.dto.CategoryCommand
import com.pokit.category.dto.toCategoriesResponse
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryImage
import com.pokit.category.model.CategoryStatus.UNCATEGORIZED
import com.pokit.category.model.OpenType
import com.pokit.category.model.duplicate
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.AlreadyExistsException
import com.pokit.common.exception.InvalidRequestException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.port.out.ContentPort
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CategoryService(
    private val categoryPort: CategoryPort,
    private val categoryImagePort: CategoryImagePort,
    private val contentPort: ContentPort
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
                openType = OpenType.PRIVATE,
            )
        )
    }

    @Transactional
    override fun update(categoryCommand: CategoryCommand, userId: Long, categoryId: Long): Category {
        val category = categoryPort.loadCategoryOrThrow(categoryId, userId)

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

    override fun getCategories(userId: Long, pageable: Pageable, filterUncategorized: Boolean): Slice<CategoriesResponse> {
        val categoriesSlice = categoryPort.loadAllByUserId(userId, pageable)

        val categories = categoriesSlice.content.map { category ->
            val contentCount = contentPort.fetchContentCountByCategoryId(category.categoryId)
            category.copy(contentCount = contentCount)
        }.map { category ->
            category.toCategoriesResponse()
        }

        val filteredCategories = if (filterUncategorized) {
            categories.filter { it.categoryName != UNCATEGORIZED.displayName }
        } else {
            categories
        }

        return SliceImpl(filteredCategories, pageable, categoriesSlice.hasNext())
    }

    override fun getCategory(userId: Long, categoryId: Long): Category =
        categoryPort.loadCategoryOrThrow(categoryId, userId)

    override fun getSharedCategory(categoryId: Long, userId: Long): Category {
        val category = categoryPort.loadByIdAndOpenType(categoryId, OpenType.PUBLIC)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)

        if (category.userId == userId) {
            throw InvalidRequestException(CategoryErrorCode.SHARE_ALREADY_EXISTS_CATEGORY)
        }

        category.apply {
            contentCount = contentPort.fetchContentCountByCategoryId(categoryId)
        }

        return category
    }

    @Transactional
    override fun completeShare(categoryId: Long, userId: Long) {
        val category = categoryPort.loadCategoryOrThrow(categoryId, userId)
            .completeShare()

        categoryPort.persist(category)
    }

    @Transactional
    override fun duplicateCategory(
        originCategoryId: Long,
        categoryName: String,
        userId: Long,
        categoryImageId: Int
    ) {
        val originCategory = categoryPort.loadByIdAndOpenType(originCategoryId, OpenType.PUBLIC)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)

        if (originCategory.userId == userId) {
            throw InvalidRequestException(CategoryErrorCode.SHARE_ALREADY_EXISTS_CATEGORY)
        }

        if (categoryPort.countByUserId(userId) >= MAX_CATEGORY_COUNT) {
            throw InvalidRequestException(CategoryErrorCode.MAX_CATEGORY_LIMIT_EXCEEDED)
        }

        if (categoryPort.existsByNameAndUserId(categoryName, userId)) {
            throw AlreadyExistsException(CategoryErrorCode.ALREADY_EXISTS_CATEGORY)
        }
        val categoryImage = (categoryImagePort.loadById(categoryImageId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY_IMAGE))
        val newCategory = categoryPort.persist(originCategory.duplicate(categoryName, userId, categoryImage))
        contentPort.duplicateContent(originCategoryId, newCategory.categoryId)
    }

    override fun getAllCategoryImages(): List<CategoryImage> =
        categoryImagePort.loadAll()

}

fun CategoryPort.loadCategoryOrThrow(categoryId: Long, userId: Long): Category {
    return loadByIdAndUserId(categoryId, userId)
        ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)
}

fun CategoryPort.loadByIdOrThrow(categoryId: Long) =
    loadById(categoryId)
        ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)
