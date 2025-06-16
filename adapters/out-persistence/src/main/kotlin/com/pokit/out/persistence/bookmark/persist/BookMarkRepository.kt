package com.pokit.out.persistence.bookmark.persist

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookMarkRepository : JpaRepository<BookmarkEntity, Long>, BookmarkQuerydslRepository {
    fun findByContentIdAndUserIdAndDeleted(
        contentId: Long,
        userId: Long,
        deleted: Boolean
    ): BookmarkEntity?

    fun findByUserIdAndDeleted(userId: Long, deleted: Boolean, pageable: Pageable): Slice<BookmarkEntity>

    fun existsByContentIdAndUserIdAndDeleted(contentId: Long, userId: Long, deleted: Boolean): Boolean

    fun countByUserIdAndDeleted(userId: Long, deleted: Boolean): Int

    @Modifying(clearAutomatically = true)
    @Query(
        """
            update BookmarkEntity b set b.deleted = true
            where b.contentId in :contentIds and b.userId = :userId and b.deleted = false
        """
    )
    fun deleteByContentIdsAndUserId(
        @Param("contentIds") contentIds: List<Long>,
        @Param("userId") userId: Long
    )
}
