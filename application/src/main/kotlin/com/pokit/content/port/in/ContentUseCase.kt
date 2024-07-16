package com.pokit.content.port.`in`

import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.user.model.User

interface ContentUseCase {
    fun bookmarkContent(user: User, contentId: Long): BookMarkContentResponse
}
