package com.pokit.out.persistence.bookmark.persist

import org.springframework.data.jpa.repository.JpaRepository

interface BookMarkRepository : JpaRepository<BookmarkEntity, Long> {
}