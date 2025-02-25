package com.pokit.out.persistence.content.persist

import com.pokit.content.model.ReportedContent
import com.pokit.out.persistence.BaseEntity
import jakarta.persistence.*

@Table(name = "REPORTED_CONTENT")
@Entity
class ReportedContentEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long = 0L,

    @Column(name = "reporter_id")
    val reporterId: Long,

    @Column(name = "content_id")
    val contentId: Long,

    @Column(name = "is_deleted")
    var isDeleted: Boolean = false,
) : BaseEntity() {
    fun delete() {
        this.isDeleted = true
    }

    companion object {
        fun of(reportedContent: ReportedContent) = ReportedContentEntity(
            id = reportedContent.id,
            reporterId = reportedContent.reporterId,
            contentId = reportedContent.contentId,
        )
    }
}

internal fun ReportedContentEntity.toDomain() = ReportedContent(
    id = this.id,
    reporterId = reporterId,
    contentId = this.contentId,
)
