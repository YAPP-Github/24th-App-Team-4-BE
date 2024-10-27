package com.pokit.out.persistence.bookmark.persist

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface BookMarkRepository : JpaRepository<BookmarkEntity, Long>, BookmarkQuerydslRepository {
    fun findByContentIdAndUserIdAndDeleted(
        contentId: Long,
        userId: Long,
        deleted: Boolean
    ): BookmarkEntity?

    fun findByUserIdAndDeleted(userId: Long, deleted: Boolean, pageable: Pageable): Slice<BookmarkEntity>

    fun existsByContentIdAndUserIdAndDeleted(contentId: Long, userId: Long, deleted: Boolean): Boolean

    fun countByUserIdAndDeleted(userId: Long, deleted: Boolean): Int
}
