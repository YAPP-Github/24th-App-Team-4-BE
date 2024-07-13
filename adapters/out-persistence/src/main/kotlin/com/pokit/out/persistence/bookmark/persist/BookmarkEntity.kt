package com.pokit.out.persistence.bookmark.persist

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
)
