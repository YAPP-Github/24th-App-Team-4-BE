package com.pokit.out.persistence.content.impl

import com.pokit.category.model.OpenType
import com.pokit.content.dto.request.ContentSearchCondition
import com.pokit.content.dto.response.ContentsResult
import com.pokit.content.dto.response.SharedContentResult
import com.pokit.content.model.Content
import com.pokit.content.port.out.ContentPort
import com.pokit.log.model.LogType
import com.pokit.out.persistence.bookmark.persist.QBookmarkEntity.bookmarkEntity
import com.pokit.out.persistence.category.persist.QCategoryEntity.categoryEntity
import com.pokit.out.persistence.content.persist.ContentEntity
import com.pokit.out.persistence.content.persist.ContentRepository
import com.pokit.out.persistence.content.persist.QContentEntity.contentEntity
import com.pokit.out.persistence.content.persist.toDomain
import com.pokit.out.persistence.log.persist.QUserLogEntity.userLogEntity
import com.querydsl.core.Tuple
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class ContentAdapter(
    private val contentRepository: ContentRepository,
    private val queryFactory: JPAQueryFactory
) : ContentPort {
    override fun loadByUserIdAndId(userId: Long, id: Long) = contentRepository.findByUserIdAndIdAndDeleted(userId, id)
        ?.run { toDomain() }

    override fun countByUserId(userId: Long): Int =
        contentRepository.countByUserId(userId)

    override fun persist(content: Content): Content {
        val contentEntity = ContentEntity.of(content)
        return contentRepository.save(contentEntity).toDomain()
    }

    override fun delete(content: Content) {
        contentRepository.findByIdOrNull(content.id)
            ?.delete()
    }

    override fun fetchContentCountByCategoryId(categoryId: Long): Int =
        contentRepository.countByCategoryIdAndDeleted(categoryId, false)


    override fun loadAllByUserIdAndContentId(
        userId: Long,
        condition: ContentSearchCondition,
        pageable: Pageable,
    ): Slice<ContentsResult> {
        val query = queryFactory.select(contentEntity, categoryEntity.name, userLogEntity.count())
            .from(contentEntity)
            .leftJoin(userLogEntity).on(userLogEntity.contentId.eq(contentEntity.id))
            .join(categoryEntity).on(categoryEntity.id.eq(contentEntity.categoryId))

        FavoriteOrNot(condition.favorites, query) // 북마크 조인 여부

        query.where(
            categoryEntity.userId.eq(userId),
            condition.categoryId?.let { categoryEntity.id.eq(it) },
            isUnread(condition.isRead),
            contentEntity.deleted.isFalse,
            dateBetween(condition.startDate, condition.endDate),
            categoryIn(condition.categoryIds),
            containsWord(condition.searchWord)
        )
            .offset(pageable.offset)
            .groupBy(contentEntity)
            .orderBy(getSortOrder(contentEntity.createdAt, "createdAt", pageable))
            .limit(pageable.pageSize + 1L)


        val contentEntityList = query.fetch()
        val hasNext = getHasNext(contentEntityList, pageable)

        val contents = contentEntityList.map {
            ContentsResult.of(
                it[contentEntity]!!.toDomain(),
                it[categoryEntity.name]!!,
                it[userLogEntity.count()]!!
            )
        }

        return SliceImpl(contents, pageable, hasNext)
    }

    override fun loadByUserIdAndCategoryName(userId: Long, categoryName: String, pageable: Pageable): Slice<ContentsResult> {
        val contents = queryFactory.select(contentEntity, categoryEntity.name, userLogEntity.count())
            .from(contentEntity)
            .leftJoin(userLogEntity).on(userLogEntity.contentId.eq(contentEntity.id))
            .join(categoryEntity).on(categoryEntity.id.eq(contentEntity.categoryId))
            .where(
                categoryEntity.userId.eq(userId),
                categoryEntity.name.eq(categoryName),
                contentEntity.deleted.isFalse,
            )
            .offset(pageable.offset)
            .groupBy(contentEntity)
            .limit((pageable.pageSize + 1).toLong())
            .orderBy(getSortOrder(contentEntity.createdAt, "createdAt", pageable))
            .fetch()

        val hasNext = getHasNext(contents, pageable)

        val contentResults = contents.map {
            ContentsResult.of(
                it[contentEntity]!!.toDomain(),
                it[categoryEntity.name]!!,
                it[userLogEntity.count()]!!
            )
        }

        return SliceImpl(contentResults, pageable, hasNext)
    }

    override fun loadBookmarkedContentsByUserId(userId: Long, pageable: Pageable): Slice<ContentsResult> {
        val contents = queryFactory.select(contentEntity, categoryEntity.name, userLogEntity.count())
            .from(contentEntity)
            .leftJoin(userLogEntity).on(userLogEntity.contentId.eq(contentEntity.id))
            .join(categoryEntity).on(categoryEntity.id.eq(contentEntity.categoryId))
            .join(bookmarkEntity).on(bookmarkEntity.contentId.eq(contentEntity.id))
            .where(
                categoryEntity.userId.eq(userId),
                contentEntity.deleted.isFalse,
            )
            .offset(pageable.offset)
            .groupBy(contentEntity)
            .limit((pageable.pageSize + 1).toLong())
            .orderBy(getSortOrder(contentEntity.createdAt, "createdAt", pageable))
            .fetch()

        val hasNext = getHasNext(contents, pageable)

        val contentResults = contents.map {
            ContentsResult.of(
                it[contentEntity]!!.toDomain(),
                it[categoryEntity.name]!!,
                it[userLogEntity.count()]!!
            )
        }

        return SliceImpl(contentResults, pageable, hasNext)
    }

    override fun loadByCategoryIdAndOpenType(categoryId: Long, opentype: OpenType, pageable: Pageable): Slice<SharedContentResult> {
        val contents = queryFactory.select(contentEntity, categoryEntity.name)
            .from(contentEntity)
            .join(categoryEntity).on(categoryEntity.id.eq(contentEntity.categoryId))
            .where(
                categoryEntity.id.eq(categoryId),
                categoryEntity.openType.eq(opentype),
                contentEntity.deleted.isFalse,
            )
            .offset(pageable.offset)
            .limit((pageable.pageSize + 1).toLong())
            .orderBy(getSortOrder(contentEntity.createdAt, "createdAt", pageable))
            .fetch()

        val hasNext = getHasNext(contents, pageable)

        val contentResults = contents.map {
            SharedContentResult.of(
                it[contentEntity]!!.toDomain(),
            )
        }

        return SliceImpl(contentResults, pageable, hasNext)
    }

    override fun loadByContentIds(contentIds: List<Long>): List<Content> =
        contentRepository.findByIdIn(contentIds)
            .map { it.toDomain() }

    private fun getHasNext(
        contentEntityList: MutableList<Tuple>,
        pageable: Pageable,
    ): Boolean {
        var hasNext = false
        if (contentEntityList.size > pageable.pageSize) {
            hasNext = true
            contentEntityList.removeAt(contentEntityList.size - 1)
        }
        return hasNext
    }

    private fun isUnread(read: Boolean?): Predicate? {
        return read?.let {
            userLogEntity.id.isNull.or(userLogEntity.type.ne(LogType.READ))
        }
    }

    private fun categoryIn(categoryIds: List<Long>?): Predicate? {
        if (categoryIds.isNullOrEmpty()) {
            return null
        }

        return contentEntity.categoryId.`in`(categoryIds)
    }

    private fun dateBetween(startDate: LocalDate?, endDate: LocalDate?): Predicate? {
        if (startDate == null || endDate == null) {
            return null
        }

        val startDateTime = startDate.atStartOfDay()
        val endDateTime = endDate.atTime(LocalTime.MAX)

        val isAfter = contentEntity.createdAt.after(startDateTime)
        val isBefore = contentEntity.createdAt.before(endDateTime)

        return isAfter.and(isBefore)
    }

    override fun deleteByUserId(userId: Long) {
        contentRepository.deleteByUserId(userId)
    }

    private fun FavoriteOrNot(
        favorites: Boolean?,
        query: JPAQuery<Tuple>
    ): JPAQuery<Tuple>? {
        return favorites
            ?.let {
                query
                    .join(bookmarkEntity)
                    .on(
                        bookmarkEntity.contentId.eq(contentEntity.id)
                            .and(bookmarkEntity.deleted.isFalse)
                    )
            }
    }

    private fun getSortOrder(property: DateTimePath<LocalDateTime>, sortField: String, pageable: Pageable): OrderSpecifier<*> {
        val order = pageable.sort.getOrderFor(sortField)
            ?.direction
            ?: Sort.Direction.ASC

        return if (order.isAscending) {
            property.asc()
        } else {
            property.desc()
        }
    }

    private fun containsWord(searchWord: String?): Predicate? {
        return searchWord?.let {
            contentEntity.title.contains(searchWord)
                .or(contentEntity.memo.contains(searchWord))
        }
    }
}
