package com.pokit.out.persistence.alert.persist

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class AlertJdbcRepositoryImpl(
    private val jdbcTemplate: JdbcTemplate
) : AlertJdbcRepository {
    override fun bulkInsert(alertEntities: List<AlertEntity>) {
        val sql = """
            INSERT INTO alert (
                user_id, content_id, content_thumb_nail
                , title, is_deleted, created_at, updated_at
            ) VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
        """.trimIndent()


        val batchArgs = alertEntities.map { alert ->
            arrayOf(
                alert.userId,
                alert.contentId,
                alert.contentThumbNail,
                alert.title,
                alert.deleted
            )
        }

        jdbcTemplate.batchUpdate(sql, batchArgs)
    }
}
