package com.pokit.out.persistence.bookmark.persist

import com.pokit.out.persistence.bookmark.persist.QBookmarkEntity.bookmarkEntity
import com.pokit.out.persistence.content.persist.QContentEntity.contentEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class BookmarkQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : BookmarkQuerydslRepository {
    override fun countByUserId(userId: Long): Int {
        return (queryFactory
            .select(bookmarkEntity.id.countDistinct())
            .from(bookmarkEntity)
            .join(contentEntity)
            .on(
                bookmarkEntity.contentId.eq(contentEntity.id),
                contentEntity.deleted.isFalse,
            )
            .where(
                bookmarkEntity.userId.eq(userId),
                bookmarkEntity.deleted.isFalse,
            )
            .fetchOne() ?: 0L).toInt()
    }

}
