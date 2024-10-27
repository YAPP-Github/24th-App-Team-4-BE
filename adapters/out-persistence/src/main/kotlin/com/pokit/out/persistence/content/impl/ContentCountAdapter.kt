package com.pokit.out.persistence.content.impl

import com.pokit.content.port.out.ContentCountPort
import com.pokit.log.model.LogType
import com.pokit.out.persistence.bookmark.persist.BookMarkRepository
import com.pokit.out.persistence.category.persist.QCategoryEntity.categoryEntity
import com.pokit.out.persistence.content.persist.QContentEntity.contentEntity
import com.pokit.out.persistence.log.persist.QUserLogEntity.userLogEntity
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class ContentCountAdapter(
    private val queryFactory: JPAQueryFactory,
    private val bookMarkRepository: BookMarkRepository
) : ContentCountPort {
    override fun getUnreadCount(userId: Long): Int {
        return queryFactory.select(contentEntity.count())
            .from(contentEntity)
            .leftJoin(userLogEntity).on(userLogEntity.contentId.eq(contentEntity.id))
            .join(categoryEntity).on(categoryEntity.id.eq(contentEntity.categoryId))
            .where(
                categoryEntity.userId.eq(userId),
                contentEntity.deleted.isFalse,
                userLogEntity.id.isNull.or(userLogEntity.type.ne(LogType.READ))
            )
            .fetchFirst()!!.toInt()
    }

    override fun getBookmarkCount(userId: Long): Int =
        bookMarkRepository.countByUserId(userId)
}
