package com.pokit.content.port.out

import com.pokit.content.model.ReportedContent

interface ReportedContentPort {
    fun persist(reportedContent: ReportedContent): ReportedContent

    fun loadAllByReporterId(reporterId: Long): List<ReportedContent>
}
