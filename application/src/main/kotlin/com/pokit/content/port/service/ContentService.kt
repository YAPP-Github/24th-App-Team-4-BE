package com.pokit.content.port.service

import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.dto.ContentCommand
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.dto.response.GetContentResponse
import com.pokit.content.dto.response.toGetContentResponse
import com.pokit.content.dto.toDomain
import com.pokit.content.exception.ContentErrorCode
import com.pokit.content.model.Content
import com.pokit.content.port.`in`.ContentUseCase
import com.pokit.content.port.out.ContentPort
import com.pokit.log.model.LogType
import com.pokit.log.model.UserLog
import com.pokit.log.port.out.UserLogPort
import com.pokit.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ContentService(
    private val contentPort: ContentPort,
    private val bookMarkPort: BookmarkPort,
    private val categoryPort: CategoryPort,
    private val userLogPort: UserLogPort
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

    @Transactional
    override fun cancelBookmark(user: User, contentId: Long) {
        verifyContent(user.id, contentId)
        bookMarkPort.delete(user.id, contentId)
    }

    override fun getContents(
        userId: Long,
        categoryId: Long,
        pageable: Pageable,
        isRead: Boolean?,
        favorites: Boolean?
    ): Slice<Content> {
        val contents = contentPort.loadAllByUserIdAndContentId(
            userId,
            categoryId,
            pageable,
            isRead,
            favorites
        )

        return contents
    }

    @Transactional
    override fun getContent(userId: Long, contentId: Long): GetContentResponse {
        val userLog = UserLog(
            contentId = contentId,
            userId = userId,
            type = LogType.READ,
            searchKeyword = null
        )
        userLogPort.persist(userLog) // 읽음 처리

        val content = verifyContent(userId, contentId)
        val bookmark = bookMarkPort.loadByContentIdAndUserId(contentId, userId)

        return bookmark
            ?.let { content.toGetContentResponse(it) } // 즐겨찾기 true
            ?: content.toGetContentResponse() // 즐겨찾기 false
    }

    override fun getRecentWord(userId: Long): List<String> {
        val userLogs = userLogPort.loadByUserIdAndType(userId, LogType.SEARCH)
        return userLogs
            .filter { true }
            .map { it.searchKeyword!! }
            .toList()
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
