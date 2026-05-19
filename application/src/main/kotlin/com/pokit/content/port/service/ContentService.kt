package com.pokit.content.port.service

import com.pokit.alert.dto.request.DiscordRequest
import com.pokit.alert.model.CreateAlertRequest
import com.pokit.bookmark.exception.BookmarkErrorCode
import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.Category
import com.pokit.category.model.CategoryStatus
import com.pokit.category.model.CategoryStatus.UNCATEGORIZED
import com.pokit.category.model.OpenType
import com.pokit.category.port.out.CategoryPort
import com.pokit.category.port.out.SharedCategoryPort
import com.pokit.common.exception.AlreadyExistsException
import com.pokit.common.exception.ClientValidationException
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.dto.request.CategorizeCommand
import com.pokit.content.dto.request.ContentCommand
import com.pokit.content.dto.request.ContentSearchCondition
import com.pokit.content.dto.request.toDomain
import com.pokit.content.dto.response.*
import com.pokit.content.exception.ContentErrorCode
import com.pokit.content.model.Content
import com.pokit.content.model.ReportReason
import com.pokit.content.model.ReportedContent
import com.pokit.content.port.`in`.ContentUseCase
import com.pokit.content.port.out.ContentCountPort
import com.pokit.content.port.out.ContentPort
import com.pokit.content.port.out.ReportedContentPort
import com.pokit.log.model.LogType
import com.pokit.log.model.UserLog
import com.pokit.log.port.out.UserLogPort
import com.pokit.notification.model.DeepLinkBuilder
import com.pokit.notification.model.NotificationType
import com.pokit.notification.port.`in`.NotificationUseCase
import com.pokit.notification.port.out.PushMessageTemplatePort
import com.pokit.user.exception.UserErrorCode
import com.pokit.user.model.InterestType
import com.pokit.user.model.User
import com.pokit.user.port.out.InterestPort
import com.pokit.user.port.out.UserPort
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional(readOnly = true)
class ContentService(
    private val contentPort: ContentPort,
    private val bookMarkPort: BookmarkPort,
    private val categoryPort: CategoryPort,
    private val userLogPort: UserLogPort,
    private val publisher: ApplicationEventPublisher,
    private val contentCountPort: ContentCountPort,
    private val interestPort: InterestPort,
    private val userPort: UserPort,
    private val reportedContentPort: ReportedContentPort,
    private val sharedCategoryPort: SharedCategoryPort,
    private val notificationUseCase: NotificationUseCase,
    private val pushMessageTemplatePort: PushMessageTemplatePort,
) : ContentUseCase {
    companion object {
        private const val MIN_CONTENT_COUNT = 3
        private const val YES = "YES"
    }

    @Transactional
    override fun bookmarkContent(user: User, contentId: Long): BookMarkContentResponse {
        verifyContent(contentId)
        bookMarkPort.loadByContentIdAndUserId(contentId, user.id)?.let {
            throw AlreadyExistsException(BookmarkErrorCode.ALREADY_EXISTS_BOOKMARK)
        }

        val bookmark = Bookmark(userId = user.id, contentId = contentId)
        val savedBookmark = bookMarkPort.persist(bookmark)
        return BookMarkContentResponse(savedBookmark.contentId)
    }

    @Transactional
    override fun create(user: User, contentCommand: ContentCommand): ContentResult {
        val category = verifyCategory(contentCommand.categoryId)
        val content = contentCommand.toDomain(user.id)
        content.parseDomain()
        val contentResult = contentPort.persist(content)
            .toGetContentResult(false, category, user)

        if (contentCommand.alertYn == YES) {
            publisher.publishEvent(CreateAlertRequest(userId = user.id, contetId = contentResult.contentId))
        }

        if (category.isShared) {
            val template = pushMessageTemplatePort.loadByType(NotificationType.LINK_ADDED)
            if (template != null) {
                val sharedMembers = sharedCategoryPort.loadByCategoryId(category.categoryId)
                sharedMembers
                    .filter { it.userId != user.id && it.alertEnabled }
                    .forEach { member ->
                        val notification = template.toNotification(
                            userId = member.userId,
                            categoryName = category.categoryName,
                            nickname = user.nickName,
                            categoryImageUrl = category.categoryImage.imageUrl,
                            deepLink = DeepLinkBuilder.forContent(category.categoryId, contentResult.contentId),
                        )
                        notificationUseCase.createAndSend(notification)
                    }
            }
        }

        return contentResult
    }

    @Transactional
    override fun update(user: User, contentCommand: ContentCommand, contentId: Long): ContentResult {
        val category = verifyCategory(contentCommand.categoryId)
        val content = verifyContent(contentId)
        content.modify(contentCommand)

        if (contentCommand.alertYn == YES) {
            publisher.publishEvent(CreateAlertRequest(userId = user.id, contetId = content.id))
        }

        return contentPort.persist(content)
            .toGetContentResult(bookMarkPort.isBookmarked(contentId, user.id), category, user)
    }

    @Transactional
    override fun delete(user: User, contentId: Long) {
        val content = verifyContent(contentId)
        bookMarkPort.delete(user.id, contentId)
        contentPort.delete(content)
    }

    @Transactional
    override fun cancelBookmark(user: User, contentId: Long) {
        verifyContent(contentId)
        bookMarkPort.delete(user.id, contentId)
    }

    private val logger = KotlinLogging.logger { }

    override fun getContents(
        userId: Long,
        condition: ContentSearchCondition,
        isPrivate: Boolean,
        pageable: Pageable,
    ): Slice<ContentsResult> {
        val category = condition.categoryId?.let { verifyCategory(it) }
        if (category != null && category.categoryName == CategoryStatus.FAVORITE.displayName) {
            val contents = contentPort.loadBookmarkedContentsByUserId(userId, pageable)
            return contents
        }

        val contents = contentPort.loadAllByUserIdAndContentId(
            userId,
            condition,
            isPrivate,
            pageable,
        )

        return contents
    }

    override fun getSharedContents(categoryId: Long, pageable: Pageable): Slice<SharedContentResult> =
        contentPort.loadByCategoryIdAndOpenType(categoryId, OpenType.PUBLIC, pageable)

    override fun getContentsByCategoryName(userId: Long, categoryName: String, pageable: Pageable): Slice<ContentsResult> =
        contentPort.loadByUserIdAndCategoryName(userId, categoryName, pageable)

    @Transactional
    override fun getContent(userId: Long, contentId: Long): ContentResult {
        val userLog = UserLog(
            contentId, userId, LogType.READ
        )
        userLogPort.persist(userLog) // 읽음 처리

        val content = verifyContent(contentId)
        val category = verifyCategory(content.categoryId)
        val bookmarkStatus = bookMarkPort.isBookmarked(contentId, userId)
        val user = userPort.loadById(content.userId)
            ?: throw NotFoundCustomException(UserErrorCode.NOT_FOUND_USER)

        return content.toGetContentResult(bookmarkStatus, category, user)
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

        val unreadContents = contentPort.loadAllByUserIdAndContentId(userId, contentSearchCondition, true, pageable)
        val remindContents = unreadContents.content.map { it.toRemindContentResult() }

        return SliceImpl(remindContents, pageable, unreadContents.hasNext())
    }

    override fun getUnreadCount(userId: Long) =
        contentCountPort.getUnreadCount(userId)

    override fun getBookmarkCount(userId: Long) =
        contentCountPort.getBookmarkCount(userId)

    @Transactional
    override fun deleteUncategorized(userId: Long, contentIds: List<Long>) {
        val category = (categoryPort.loadByNameAndUserId(UNCATEGORIZED.displayName, userId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_UNCATEGORIZED))

        val contents = contentPort.loadByContentIds(contentIds)
        contents.forEach {
            if (it.categoryId != category.categoryId) {
                throw ClientValidationException(ContentErrorCode.NOT_UNCATEGORIZED_CONTENT)
            }
        }

        contentPort.deleteAllByIds(contentIds)
    }

    @Transactional
    override fun categorize(userId: Long, command: CategorizeCommand) {
        val category = verifyCategory(command.categoryId)
        val contents = contentPort.loadAllByUserIdAndContentIds(userId, command.contentIds)
        contentPort.updateCategoryId(contents, category.categoryId)
    }

    @Transactional
    override fun updateThumbnail(userId: Long, contentId: Long, thumbnail: String): Content {
        val content = verifyContent(contentId)
        content.modifyThumbnail(thumbnail)
        return contentPort.persist(content)
    }

    override fun getRecommendedContent(userId: Long, keyword: String?, pageable: Pageable): Slice<ContentsResult> {
        val searchKeyword = keyword?.let {
            listOf(InterestType.of(it))
        } ?: interestPort.loadByUserId(userId).map {
            it.interestType
        }
        return contentPort.loadAllByKeyword(userId, searchKeyword, pageable)
    }

    @Transactional
    override fun report(userId: Long, contentId: Long, reportReason: ReportReason) {
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
        val content = verifyContent(contentId)

        val request = DiscordRequest(
            reportedContentId = contentId,
            reporterId = userId,
            contentsUserId = content.userId,
            data = content.data,
            reportReason = reportReason,
            createdAt = formatter.format(content.createdAt),
        )

        val reportedContent = ReportedContent(
            reporterId = userId,
            contentId = contentId,
            reportReason = reportReason,
        )

        publisher.publishEvent(request)
        reportedContentPort.persist(reportedContent)
    }

    private fun verifyContent(contentId: Long): Content {
        return contentPort.loadById(contentId)
            ?: throw NotFoundCustomException(ContentErrorCode.NOT_FOUND_CONTENT)
    }

    private fun verifyCategory(categoryId: Long): Category {
        return categoryPort.loadById(categoryId)
            ?: throw NotFoundCustomException(CategoryErrorCode.NOT_FOUND_CATEGORY)
    }
}
