package com.pokit.out.persistence.bookmark.impl

import com.pokit.bookmark.model.Bookmark
import com.pokit.bookmark.port.out.BookmarkPort
import com.pokit.out.persistence.bookmark.persist.BookMarkRepository
import com.pokit.out.persistence.bookmark.persist.BookmarkEntity
import com.pokit.out.persistence.bookmark.persist.toDomain
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository

@Repository
class BookMarkAdapter(
    private val bookMarkRepository: BookMarkRepository
) : BookmarkPort {
    override fun persist(bookmark: Bookmark): Bookmark {
        val bookmarkEntity = BookmarkEntity.of(bookmark)
        val savedBookmark = bookMarkRepository.save(bookmarkEntity)
        return savedBookmark.toDomain()
    }

    override fun delete(contentId: Long, userId: Long) {
        bookMarkRepository.findByContentIdAndUserIdAndDeleted(
            contentId,
            userId,
            false
        )?.delete()
    }

    override fun loadByContentIdAndUserId(contentId: Long, userId: Long): Bookmark? {
        return bookMarkRepository.findByContentIdAndUserIdAndDeleted(
            contentId,
            userId,
            false
        )?.toDomain()
    }

    override fun loadByUserId(userId: Long, pageable: Pageable): Slice<Bookmark> =
        bookMarkRepository.findByUserIdAndDeleted(userId, false, pageable)
            .map { it.toDomain() }

}
