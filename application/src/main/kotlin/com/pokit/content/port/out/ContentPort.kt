package com.pokit.content.port.out

import com.pokit.category.model.OpenType
import com.pokit.content.dto.request.ContentSearchCondition
import com.pokit.content.dto.response.ContentsResult
import com.pokit.content.dto.response.SharedContentResult
import com.pokit.content.model.Content
import com.pokit.content.model.ContentWithUser
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface ContentPort {
    fun loadByUserIdAndId(userId: Long, id: Long): Content?

    fun countByUserId(userId: Long): Int

    fun persist(content: Content): Content

    fun delete(content: Content)

    fun fetchContentCountByCategoryId(categoryId: Long): Int

    fun loadAllByUserIdAndContentId(
        userId: Long,
        condition: ContentSearchCondition,
        pageable: Pageable,
    ): Slice<ContentsResult>

    fun loadByUserIdAndCategoryName(
        userId: Long,
        categoryName: String,
        pageable: Pageable,
    ): Slice<ContentsResult>

    fun deleteByUserId(userId: Long)

    fun loadByContentIds(contentIds: List<Long>): List<Content>

    fun loadBookmarkedContentsByUserId(userId: Long, pageable: Pageable): Slice<ContentsResult>

    fun loadByCategoryIdAndOpenType(
        categoryId: Long,
        opentype: OpenType,
        pageable: Pageable
    ): Slice<SharedContentResult>

    fun duplicateContent(originCategoryId: Long, targetCategoryId: Long)

    fun loadByContentIdsWithUser(contetIds: List<Long>): List<ContentWithUser>
    fun deleteAllByIds(contentIds: List<Long>)
}
