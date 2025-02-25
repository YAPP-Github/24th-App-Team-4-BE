package com.pokit.out.persistence.content.persist

import org.springframework.data.jpa.repository.JpaRepository

interface ReportedContentRepository : JpaRepository<ReportedContentEntity, Long> {
    fun findAllByReporterId(reporterId: Long): List<ReportedContentEntity>
}
