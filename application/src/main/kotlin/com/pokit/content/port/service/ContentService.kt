package com.pokit.content.port.service

import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.model.RemindCategory
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.dto.request.ContentCommand
import com.pokit.content.dto.request.ContentSearchCondition
import com.pokit.content.dto.request.toDomain
import com.pokit.content.dto.response.*
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
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class ContentService(
    private val contentPort: ContentPort,
    private val bookMarkPort: BookmarkPort,
    private val categoryPort: CategoryPort,
    private val userLogPort: UserLogPort
) : ContentUseCase {
    companion object {
        private const val MIN_CONTENT_COUNT = 3
    }

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
        val content = contentCommand.toDomain()
        content.parseDomain()
        return contentPort.persist(content)
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
        condition: ContentSearchCondition,
        pageable: Pageable,
    ): Slice<ContentsResult> {
        val contents = contentPort.loadAllByUserIdAndContentId(
            userId,
            condition,
            pageable,
        )

        return contents
    }

    override fun getContentsByCategoryName(userId: Long, categoryName: String, pageable: Pageable): Slice<ContentsResult> =
        contentPort.loadByUserIdAndCategoryName(userId, categoryName, pageable)


    @Transactional
    override fun getContent(userId: Long, contentId: Long): GetContentResult {
        val userLog = UserLog(
            contentId, userId, LogType.READ
        )
        userLogPort.persist(userLog) // 읽음 처리

        val content = verifyContent(userId, contentId)
        val category = verifyCategory(content.categoryId, userId)
        val bookmarkStatus = bookMarkPort.isBookmarked(contentId, userId)

        return content.toGetContentResult(bookmarkStatus, category)
    }

    override fun getBookmarkContents(userId: Long, pageable: Pageable): Slice<RemindContentResult> {
        val bookMarks = contentPort.loadBookmarkedContentsByUserId(userId, pageable)
            .map { it.toRemindContentResult() }

        return SliceImpl(bookMarks.content, pageable, bookMarks.hasNext())
    }

    override fun getUnreadContents(userId: Long, pageable: Pageable): Slice<RemindContentResult> {
        val contentSearchCondition = ContentSearchCondition(
            isRead = false,
            categoryId = null,
            favorites = null,
            startDate = null,
            endDate = null,
            categoryIds = null,
            searchWord = null
        )

        val unreadContents = contentPort.loadAllByUserIdAndContentId(userId, contentSearchCondition, pageable)
        val remindContents = unreadContents.content.map { it.toRemindContentResult() }

        return SliceImpl(remindContents, pageable, unreadContents.hasNext())
    }

    override fun getTodayContents(userId: Long): List<RemindContentResult> {
//        if (contentPort.countByUserId(userId) < MIN_CONTENT_COUNT) {
//            return emptyList()
//        }

        //TODO 알고리즘 구현
        return listOf(
            RemindContentResult(
                1L,
                RemindCategory(1L, "category"),
                "title",
                "url",
                LocalDateTime.now(),
                "domain",
                false,
                "thumbNail"
            ),
            RemindContentResult(
                2L,
                RemindCategory(1L, "category"),
                "양궁 올림픽 덜덜",
                "https://www.youtube.com/watch?v=s9L9yRL_I0s",
                LocalDateTime.now(),
                "youtu.be",
                true,
                "thumbNail"
            ),
            RemindContentResult(
                3L,
                RemindCategory(1L, "category"),
                "요리 맛있겠땅",
                "https://www.youtube.com/shorts/aLZEwkm4tGU",
                LocalDateTime.now(),
                "youtu.be",
                true,
                "thumbNail"
            ),
        )
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
