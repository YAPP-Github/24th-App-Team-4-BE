package com.pokit.content.port.service

import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.common.exception.NotFoundCustomException
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.exception.ContentErrorCode
import com.pokit.content.model.Content
import com.pokit.content.port.`in`.ContentUseCase
import com.pokit.content.port.out.ContentPort
import com.pokit.user.model.User
import org.springframework.stereotype.Service

@Service
class ContentService(
    private val contentPort: ContentPort,
    private val bookMarkPort: BookmarkPort
) : ContentUseCase {
    override fun bookmarkContent(user: User, contentId: Long): BookMarkContentResponse {
        verifyContent(contentId)
        val bookmark = Bookmark(userId = user.id, contentId = contentId)
        val savedBookmark = bookMarkPort.persist(bookmark)
        return BookMarkContentResponse(savedBookmark.contentId)
    }

    private fun verifyContent(contentId: Long): Content {
        return contentPort.loadById(contentId)
            ?: throw NotFoundCustomException(ContentErrorCode.NOT_FOUND_CONTENT)
    }
}
