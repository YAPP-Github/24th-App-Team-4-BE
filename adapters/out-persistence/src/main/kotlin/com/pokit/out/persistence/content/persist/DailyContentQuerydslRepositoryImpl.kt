package com.pokit.out.persistence.content.persist

import com.pokit.out.persistence.category.persist.QCategoryEntity.categoryEntity
import com.pokit.out.persistence.content.persist.QContentEntity.contentEntity
import com.pokit.out.persistence.content.persist.QDailyContentEntity.dailyContentEntity
import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class DailyContentQuerydslRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : DailyContentQuerydslRepository {
    override fun getDailyContentsByUserId(userId: Long): MutableList<Tuple> =
        queryFactory.select(contentEntity, categoryEntity.name)
            .from(contentEntity)
            .join(dailyContentEntity).on(contentEntity.id.eq(dailyContentEntity.contentId))
            .join(categoryEntity).on(contentEntity.categoryId.eq(categoryEntity.id))
            .where(dailyContentEntity.userId.eq(userId))
            .fetch()

    override fun getContentIdsByUserId(userId: Long): List<Long> =
        queryFactory
            .select(contentEntity.id)
            .from(contentEntity)
            .join(categoryEntity).on(contentEntity.categoryId.eq(categoryEntity.id))
            .where(
                categoryEntity.userId.eq(userId)
                    .and(categoryEntity.deleted.eq(false))
                    .and(contentEntity.deleted.eq(false))
            )
            .fetch()
}
