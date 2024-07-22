package com.pokit.content.port.`in`

import com.pokit.content.dto.ContentCommand
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.model.Content
import com.pokit.user.model.User

interface ContentUseCase {
    fun bookmarkContent(user: User, contentId: Long): BookMarkContentResponse

    fun create(user: User, contentCommand: ContentCommand): Content

    fun update(user: User, contentCommand: ContentCommand, contentId: Long): Content

    fun delete(user: User, contentId: Long)
}
