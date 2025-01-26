package com.pokit.category.port.service

import com.pokit.category.dto.CategoriesResponse
import com.pokit.category.dto.CategoryCommand
import com.pokit.category.dto.toCategoriesResponse
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.*
import com.pokit.category.model.CategoryStatus.FAVORITE
import com.pokit.category.model.CategoryStatus.UNCATEGORIZED
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.category.port.out.CategoryImagePort
import com.pokit.category.port.out.CategoryPort
import com.pokit.category.port.out.SharedCategoryPort
import com.pokit.common.exception.AlreadyExistsException
import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.InvalidRequestException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.port.out.ContentPort
import com.pokit.user.model.User
import com.pokit.user.port.out.UserPort
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
    private val contentPort: ContentPort,
    private val sharedCategoryPort: SharedCategoryPort,
    private val userPort: UserPort,
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

        val category = categoryPort.persist(
            Category(
                categoryName = command.categoryName,
                categoryImage = categoryImage,
                userId = userId,
                openType = command.openType,
                keyword = command.keywordType,
                ownerId = userId
            )
        )

        val sharedCategory = SharedCategory(
            userId = userId,
            categoryId = category.categoryId
        )

        sharedCategoryPort.persist(sharedCategory)

        return category
    }

    @Transactional
    override fun update(categoryCommand: CategoryCommand, userId: Long, categoryId: Long): Category {
        val category = categoryPort.loadCategoryOrThrow(categoryId, userId)

        val categoryImage = categoryImagePort.loadById(categoryCommand.categoryImageId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY_IMAGE)

        category.update(categoryCommand, categoryImage)
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
            category.toCategoriesResponse(category.isFavorite)
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
    override fun completeShare(categoryId: Long) {
        val category = categoryPort.loadByIdOrThrow(categoryId)
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

    @Transactional
    override fun acceptCategory(userId: Long, categoryId: Long) {
        val category = categoryPort.loadByIdOrThrow(categoryId)

        sharedCategoryPort.loadByUserIdAndCategoryId(userId, categoryId)
            ?.let { throw ClientValidationException(CategoryErrorCode.ALREADY_ACCEPTED) }

        category.addUserCount()
        category.shared()

        categoryPort.persist(category)

        val sharedCategory = SharedCategory(
            userId = userId,
            categoryId = category.categoryId,
        )
        sharedCategoryPort.persist(sharedCategory)
    }

    @Transactional
    override fun resignUser(userId: Long, categoryId: Long, resignUserId: Long) {
        val category = categoryPort.loadCategoryOrThrow(categoryId, userId)
        if (category.userId != userId) {
            throw ClientValidationException(CategoryErrorCode.NOT_OWNER)
        }
        val sharedCategory = (sharedCategoryPort.loadByUserIdAndCategoryId(resignUserId, category.categoryId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NEVER_ACCPTED))

        sharedCategoryPort.delete(sharedCategory)

        category.minusUserCount() // 포킷 인원수 감소
        categoryPort.persist(category)
    }

    @Transactional
    override fun outCategory(userId: Long, categoryId: Long) {
        val category = categoryPort.loadByIdOrThrow(categoryId)

        val sharedCategory = sharedCategoryPort.loadByUserIdAndCategoryId(userId, categoryId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NEVER_ACCPTED)
        sharedCategoryPort.delete(sharedCategory)

        if (category.ownerId == userId) { // 나간사람이 방장이었다면
            val firstSharedCategory = sharedCategoryPort.loadFirstByCategoryId(categoryId)
                ?: throw ClientValidationException(CategoryErrorCode.EMPTY_USER_IN_CATEGORY)
            category.ownerId = firstSharedCategory.userId // 권한 위임
        }

        category.minusUserCount() // 포킷 인원수 감소
        categoryPort.persist(category)
    }

    override fun getCategoriesV2(userId: Long, pageable: Pageable, filterUncategorized: Boolean, filterFavorite: Boolean): Slice<CategoriesResponse> {
        val sharedCategories = sharedCategoryPort.loadByUserId(userId)
        val categoryIds = sharedCategories.map { it.categoryId }
        val categoriesSlice = categoryPort.loadAllInId(categoryIds, pageable)

        val bookmark = contentPort.loadBookmarkedContentsByUserId(userId, pageable)
        val favoriteCategory = categoryPort.loadByNameAndUserId(FAVORITE.displayName, userId)
        favoriteCategory!!.copy(contentCount = bookmark.size)

        val categories = categoriesSlice.content.map { category ->
            val contentCount = contentPort.fetchContentCountByCategoryId(category.categoryId)
            category.copy(contentCount = contentCount)
        }.map { category ->
            category.toCategoriesResponse(category.isFavorite)
        }.toMutableList()

        if (!filterFavorite) {
            val favoriteResponse = favoriteCategory.toCategoriesResponse(favoriteCategory.isFavorite)
            categories.add(0, favoriteResponse)
        }

        val filteredCategories = if (filterUncategorized) {
            categories.filter { it.categoryName != UNCATEGORIZED.displayName }
        } else {
            categories
        }

        return SliceImpl(filteredCategories, pageable, categoriesSlice.hasNext())
    }

    override fun getInvitedUsers(userId: Long, categoryId: Long): List<User> {
        val sharedCategories = sharedCategoryPort.loadByCategoryId(categoryId)
        val userIds = sharedCategories.map { it.userId }
        val users = userPort.loadAllInIds(userIds)
        return users
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
