package com.pokit.content.port.service

import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.dto.ContentCommand
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.dto.toDomain
import com.pokit.content.exception.ContentErrorCode
import com.pokit.content.model.Content
import com.pokit.content.port.`in`.ContentUseCase
import com.pokit.content.port.out.ContentPort
import com.pokit.user.model.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ContentService(
    private val contentPort: ContentPort,
    private val bookMarkPort: BookmarkPort,
    private val categoryPort: CategoryPort
) : ContentUseCase {

    @Transactional
    override fun bookmarkContent(user: User, contentId: Long): BookMarkContentResponse {
        verifyContent(user.id, contentId)
        val bookmark = Bookmark(userId = user.id, contentId = contentId)
        val savedBookmark = bookMarkPort.persist(bookmark)
        return BookMarkContentResponse(savedBookmark.contentId)
    }

    @Transactional
    override fun create(user: User, contentCommand: ContentCommand): Content {
        verifyCategory(contentCommand.categoryId, user.id)
        return contentPort.persist(
            contentCommand.toDomain()
        )
    }

    @Transactional
    override fun update(user: User, contentCommand: ContentCommand, contentId: Long): Content {
        val content = verifyContent(user.id, contentId)
        verifyCategory(contentCommand.categoryId, user.id)

        content.modify(contentCommand)
        return contentPort.persist(content)
    }

    @Transactional
    override fun delete(user: User, contentId: Long) {
        val content = verifyContent(user.id, contentId)
        contentPort.delete(content)
    }

    private fun verifyContent(userId: Long, contentId: Long): Content {
        return contentPort.loadByUserIdAndId(userId, contentId)
            ?: throw NotFoundCustomException(ContentErrorCode.NOT_FOUND_CONTENT)
    }

    private fun verifyCategory(categoryId: Long, userId: Long): Category {
        return categoryPort.loadByIdAndUserId(categoryId, userId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)
    }
}
