package com.pokit.out.persistence.content.impl

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
import com.pokit.out.persistence.user.persist.QUserEntity.userEntity
import com.querydsl.core.types.dsl.DateTimePath
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class ContentAdapter(
    private val contentRepository: ContentRepository,
    private val queryFactory: JPAQueryFactory
) : ContentPort {
    override fun loadByUserIdAndId(userId: Long, id: Long) = contentRepository.findByUserIdAndIdAndDeleted(userId, id)
        ?.run { toDomain() }

    override fun persist(content: Content): Content {
        val contentEntity = ContentEntity.of(content)
        return contentRepository.save(contentEntity).toDomain()
    }

    override fun delete(content: Content) {
        contentRepository.findByIdOrNull(content.id)
            ?.delete()
    }

    override fun fetchContentCountByCategoryId(categoryId: Long): Int =
        contentRepository.countByCategoryId(categoryId)


    override fun loadAllByUserIdAndContentId(
        userId: Long,
        categoryId: Long,
        pageable: Pageable,
        read: Boolean?,
        favorites: Boolean?
    ): Slice<Content> {
        var hasNext = false
        val order = pageable.sort.getOrderFor("createdAt")

        val query = queryFactory.select(contentEntity)
            .from(contentEntity)
            .join(categoryEntity).on(categoryEntity.id.eq(contentEntity.categoryId))
            .join(userEntity).on(userEntity.id.eq(categoryEntity.userId))

        FavoriteOrNot(favorites, query) // 북마크 조인 여부
        ReadOrNot(read, query) // 읽음 로그 조인 여부

        query.where(
            userEntity.id.eq(userId),
            categoryEntity.id.eq(categoryId),
            contentEntity.deleted.isFalse
        )
            .orderBy(getSort(contentEntity.createdAt, order!!))
            .limit(pageable.pageSize + 1L)


        val contentEntityList = query.fetch()

        if (contentEntityList.size > pageable.pageSize) {
            hasNext = true
            contentEntityList.removeAt(contentEntityList.size - 1)
        }

        val contents = contentEntityList.map { it.toDomain() }

        return SliceImpl(contents, pageable, hasNext)
    }

    private fun ReadOrNot(
        read: Boolean?,
        query: JPAQuery<ContentEntity>
    ): JPAQuery<ContentEntity>? {
        return read
            ?.let {
                query
                    .leftJoin(userLogEntity)
                    .on(userLogEntity.contentId.eq(contentEntity.id))
                    .where(userLogEntity.id.isNull.or(userLogEntity.type.ne(LogType.READ)))
            }
    }

    private fun FavoriteOrNot(
        favorites: Boolean?,
        query: JPAQuery<ContentEntity>
    ): JPAQuery<ContentEntity>? {
        return favorites
            ?.let {
                query
                    .join(bookmarkEntity)
                    .on(bookmarkEntity.contentId.eq(contentEntity.id)
                            .and(bookmarkEntity.deleted.isFalse))
            }
    }

    private fun getSort(property: DateTimePath<LocalDateTime>, order: Sort.Order) =
        if (order.isDescending) property.desc()
        else property.asc()

}
