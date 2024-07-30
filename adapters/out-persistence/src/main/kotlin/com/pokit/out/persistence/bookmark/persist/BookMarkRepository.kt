package com.pokit.out.persistence.bookmark.persist

import org.springframework.data.jpa.repository.JpaRepository

interface BookMarkRepository : JpaRepository<BookmarkEntity, Long> {
    fun findByContentIdAndUserIdAndDeleted(
        contentId: Long,
        userId: Long,
        deleted: Boolean
    ): BookmarkEntity?

    fun findTop3ByUserIdOrderByCreatedAtDesc(userId: Long): List<BookmarkEntity>

    fun existsByContentIdAndUserIdAndDeleted(contentId: Long, userId: Long, deleted: Boolean): Boolean
}
