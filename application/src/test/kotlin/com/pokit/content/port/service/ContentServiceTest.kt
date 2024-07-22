package com.pokit.content.port.service

import com.pokit.bookmark.BookmarkFixture
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.ContentFixture
import com.pokit.content.port.out.ContentPort
import com.pokit.user.UserFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ContentServiceTest : BehaviorSpec({
    val contentPort = mockk<ContentPort>()
    val bookmarkPort = mockk<BookmarkPort>()
    val contentService = ContentService(contentPort, bookmarkPort)

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
})
