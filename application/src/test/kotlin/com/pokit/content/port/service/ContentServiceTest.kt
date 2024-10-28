package com.pokit.content.port.service

import com.pokit.alert.model.CreateAlertRequest
import com.pokit.bookmark.BookmarkFixture
import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.category.CategoryFixture
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.ContentFixture
import com.pokit.content.dto.request.toDomain
import com.pokit.content.model.Content
import com.pokit.content.port.out.ContentCountPort
import com.pokit.content.port.out.ContentPort
import com.pokit.log.port.out.UserLogPort
import com.pokit.user.UserFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.springframework.context.ApplicationEventPublisher

class ContentServiceTest : BehaviorSpec({
    val contentPort = mockk<ContentPort>()
    val bookmarkPort = mockk<BookmarkPort>()
    val categoryPort = mockk<CategoryPort>()
    val userLogPort = mockk<UserLogPort>()
    val publisher = mockk<ApplicationEventPublisher>()
    val contentCountPort = mockk<ContentCountPort>()
    val contentService = ContentService(contentPort, bookmarkPort, categoryPort, userLogPort, publisher, contentCountPort)

    Given("컨텐츠에 대해 즐겨찾기 할 때") {
        val user = UserFixture.getUser()
        val requestContentId = 1L
        val invalidContentId = 2L
        val content = ContentFixture.getContent()
        val bookmark = BookmarkFixture.getBookmark(requestContentId, user.id)

        every { contentPort.loadByUserIdAndId(user.id, requestContentId) } returns content
        every { contentPort.loadByUserIdAndId(user.id, invalidContentId) } returns null
        every { bookmarkPort.persist(any(Bookmark::class)) } returns bookmark
        every { bookmarkPort.loadByContentIdAndUserId(requestContentId, user.id) } returns null

        When("존재하지 않는 컨텐츠면") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    contentService.bookmarkContent(user, invalidContentId)
                }
            }
        }

        When("존재하는 컨텐츠면") {
            val response = contentService.bookmarkContent(user, requestContentId)
            Then("북마크 처리 후 컨텐츠 아이디가 반환된다.") {
                response.contentId shouldBe requestContentId
            }
        }
    }

    Given("컨텐츠 관련 요청이 들어올 때") {
        val invalidContentId = 100L
        val content = ContentFixture.getContent()

        val command = ContentFixture.getContentCommand(1L)
        val createContent = command.toDomain()
        createContent.parseDomain()

        val invalidCommand = ContentFixture.getContentCommand(2L)

        val user = UserFixture.getUser()
        val category = CategoryFixture.getCategory(user.id)

        every {
            categoryPort.loadByIdAndUserId(command.categoryId, user.id)
        } returns category

        every {
            contentPort.persist(any(Content::class))
        } returns createContent

        every {
            contentPort.loadByUserIdAndId(user.id, createContent.id)
        } returns createContent

        every {
            contentPort.loadByUserIdAndId(user.id, invalidContentId)
        } returns null

        every {
            categoryPort.loadByIdAndUserId(invalidCommand.categoryId, user.id)
        } returns null

        every {
            bookmarkPort.isBookmarked(content.id, user.id)
        } returns true

        every {
            publisher.publishEvent(any(CreateAlertRequest::class))
        } returns Unit

        When("저장 요청이 들어오면") {
            val content = contentService.create(user, command)
            Then("저장 후 저장 된 컨텐츠를 반환한다.") {
                content.data shouldBe command.data
                content.title shouldBe command.title
                content.memo shouldBe command.memo
                content.category.categoryId shouldBe command.categoryId
                content.alertYn shouldBe command.alertYn
            }
        }
        When("존재하지 않는 카테고리에 대한 저장 시") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    contentService.create(user, invalidCommand)
                }
            }
        }

        When("수정 요청이 들어오면") {
            val modifiedContent = contentService.update(user, command, content.id)
            Then("수정된 컨텐츠가 반환된다.") {
                modifiedContent.data shouldBe command.data
                modifiedContent.title shouldBe command.title
                modifiedContent.memo shouldBe command.memo
                modifiedContent.category.categoryId shouldBe command.categoryId
                modifiedContent.alertYn shouldBe command.alertYn
            }
        }

        When("존재하지 않는 컨텐츠를 수정하려 하면") {
            Then("예외가 발생한다.") {
                shouldThrow<NotFoundCustomException> {
                    contentService.update(user, command, invalidContentId)
                }
            }
        }
    }
})
