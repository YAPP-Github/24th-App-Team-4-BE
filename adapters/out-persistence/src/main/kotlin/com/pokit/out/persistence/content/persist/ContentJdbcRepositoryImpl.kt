package com.pokit.out.persistence.content.persist

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ContentJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : ContentJdbcRepository {
    override fun bulkInsert(contentEntities: List<ContentEntity>) {
        val sql = """
            INSERT INTO content (
                category_id, type, data, title, memo, alert_yn, domain, is_deleted, thumb_nail, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        """.trimIndent()


        val batchArgs = contentEntities.map { content ->
            arrayOf(
                content.categoryId,
                content.type.name,
                content.data,
                content.title,
                content.memo,
                content.alertYn,
                content.domain,
                content.deleted,
                content.thumbNail
            )
        }

        jdbcTemplate.batchUpdate(sql, batchArgs)
    }
}
