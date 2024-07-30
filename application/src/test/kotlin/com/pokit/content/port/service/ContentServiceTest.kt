package com.pokit.content.port.service

import com.pokit.bookmark.BookmarkFixture
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.category.CategoryFixture
import com.pokit.category.port.out.CategoryPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.ContentFixture
import com.pokit.content.dto.toDomain
import com.pokit.content.model.Content
import com.pokit.content.port.out.ContentPort
import com.pokit.log.model.LogType
import com.pokit.log.port.out.UserLogPort
import com.pokit.user.UserFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ContentServiceTest : BehaviorSpec({
    val contentPort = mockk<ContentPort>()
    val bookmarkPort = mockk<BookmarkPort>()
    val categoryPort = mockk<CategoryPort>()
    val userLogPort = mockk<UserLogPort>()
    val contentService = ContentService(contentPort, bookmarkPort, categoryPort, userLogPort)

    Given("컨텐츠에 대해 즐겨찾기 할 때") {
        val user = UserFixture.getUser()
        val requestContentId = 1L
        val invalidContentId = 2L
        val content = ContentFixture.getContent()
        val bookmark = BookmarkFixture.getBookmark(requestContentId, user.id)

        every { contentPort.loadByUserIdAndId(user.id, requestContentId) } returns content
        every { contentPort.loadByUserIdAndId(user.id, invalidContentId) } returns null
        every { bookmarkPort.persist(bookmark) } returns bookmark

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
        val invalidCommand = ContentFixture.getContentCommand(2L)

        val user = UserFixture.getUser()
        val category = CategoryFixture.getCategory(user.id)

        val userLog = UserFixture.getUserLog(LogType.SEARCH, "검색어")

        every {
            categoryPort.loadByIdAndUserId(command.categoryId, user.id)
        } returns category

        every {
            contentPort.persist(any(Content::class))
        } returns command.toDomain()

        every {
            contentPort.loadByUserIdAndId(user.id, content.id)
        } returns content

        every {
            contentPort.loadByUserIdAndId(user.id, invalidContentId)
        } returns null

        every {
            categoryPort.loadByIdAndUserId(invalidCommand.categoryId, user.id)
        } returns null

        every {
            userLogPort.loadByUserIdAndType(user.id, LogType.SEARCH)
        } returns mutableListOf(userLog)

        When("저장 요청이 들어오면") {
            val content = contentService.create(user, command)
            Then("저장 후 저장 된 컨텐츠를 반환한다.") {
                content.data shouldBe command.data
                content.title shouldBe command.title
                content.memo shouldBe command.memo
                content.categoryId shouldBe command.categoryId
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
                modifiedContent.categoryId shouldBe command.categoryId
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

        When("최근 검색어 목록을 조회하면") {
            val words = contentService.getRecentWord(user.id)
            Then("로그 타입이 SEARCH인 유저 로그들이 조회된다.") {
                words.isNotEmpty()
                words[0] shouldBe userLog.searchKeyword
            }
        }
    }
})
