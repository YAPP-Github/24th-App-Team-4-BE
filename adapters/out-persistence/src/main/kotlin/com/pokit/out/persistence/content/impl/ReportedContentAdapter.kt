package com.pokit.out.persistence.content.impl

import com.pokit.content.model.ReportedContent
import com.pokit.content.port.out.ReportedContentPort
import com.pokit.out.persistence.content.persist.ReportedContentEntity
import com.pokit.out.persistence.content.persist.ReportedContentRepository
import com.pokit.out.persistence.content.persist.toDomain
import org.springframework.stereotype.Repository

@Repository
class ReportedContentAdapter(
    private val reportedContentRepository: ReportedContentRepository
) : ReportedContentPort {
    override fun persist(reportedContent: ReportedContent): ReportedContent {
        return reportedContentRepository.save(ReportedContentEntity.of(reportedContent))
            .toDomain()
    }

    override fun loadAllByReporterId(reporterId: Long): List<ReportedContent> {
        return reportedContentRepository.findAllByReporterId(reporterId)
            .map { it.toDomain() }
    }
}
