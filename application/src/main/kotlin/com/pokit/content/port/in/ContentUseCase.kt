package com.pokit.content.port.`in`

import com.pokit.content.dto.request.ContentCommand
import com.pokit.content.dto.request.ContentSearchCondition
import com.pokit.content.dto.response.*
import com.pokit.user.model.User
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface ContentUseCase {
    fun bookmarkContent(user: User, contentId: Long): BookMarkContentResponse

    fun create(user: User, contentCommand: ContentCommand): ContentResult

    fun update(user: User, contentCommand: ContentCommand, contentId: Long): ContentResult

    fun delete(user: User, contentId: Long)

    fun cancelBookmark(user: User, contentId: Long)

    fun getContents(
        userId: Long,
        condition: ContentSearchCondition,
        pageable: Pageable,
    ): Slice<ContentsResult>

    fun getSharedContents(
        categoryId: Long,
        pageable: Pageable,
    ): Slice<SharedContentResult>

    fun getContentsByCategoryName(userId: Long, categoryName: String, pageable: Pageable): Slice<ContentsResult>

    fun getContent(userId: Long, contentId: Long): ContentResult

    fun getBookmarkContents(userId: Long, pageable: Pageable): Slice<RemindContentResult>

    fun getUnreadContents(userId: Long, pageable: Pageable): Slice<RemindContentResult>
}
