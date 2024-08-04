package com.pokit.content.port.out

import com.pokit.content.dto.response.ContentsResult
import com.pokit.content.dto.request.ContentSearchCondition
import com.pokit.content.model.Content
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface ContentPort {
    fun loadByUserIdAndId(userId: Long, id: Long): Content?

    fun persist(content: Content): Content

    fun delete(content: Content)

    fun fetchContentCountByCategoryId(categoryId: Long): Int

    fun loadAllByUserIdAndContentId(
        userId: Long,
        condition: ContentSearchCondition,
        pageable: Pageable,
    ): Slice<ContentsResult>

    fun deleteByUserId(userId: Long)

    fun loadByContentIds(contentIds: List<Long>): List<Content>
}
