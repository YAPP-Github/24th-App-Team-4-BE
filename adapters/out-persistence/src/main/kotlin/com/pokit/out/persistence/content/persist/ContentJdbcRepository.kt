package com.pokit.out.persistence.content.persist

interface ContentJdbcRepository {
    fun bulkInsert(contentEntity: List<ContentEntity>)
}
