package com.pokit.out.persistence.bookmark.persist

import com.pokit.bookmark.model.Bookmark
import jakarta.persistence.*

// 콕 엔티티
@Table(name = "BOOKMARK")
@Entity
class BookmarkEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0L,

    @Column(name = "content_id")
    val contentId: Long,

    @Column(name = "user_id")
    val userId: Long,
) {
    companion object {
        fun of(bookmark: Bookmark) =
            BookmarkEntity(
                contentId = bookmark.contentId,
                userId = bookmark.userId
            )
    }
}

fun BookmarkEntity.toDomain() = Bookmark(
    contentId = this.contentId,
    userId = this.userId
)
