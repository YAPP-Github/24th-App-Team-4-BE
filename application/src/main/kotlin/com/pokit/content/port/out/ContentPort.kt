package com.pokit.content.port.out

import com.pokit.content.model.Content
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface ContentPort {
    fun loadByUserIdAndId(userId: Long, id: Long): Content?

    fun persist(content: Content): Content

    fun delete(content: Content)
    fun loadAllByUserIdAndContentId(
        userId: Long,
        categoryId: Long,
        pageable: Pageable,
        read: Boolean?,
        favorites: Boolean?
    ): Slice<Content>
}
