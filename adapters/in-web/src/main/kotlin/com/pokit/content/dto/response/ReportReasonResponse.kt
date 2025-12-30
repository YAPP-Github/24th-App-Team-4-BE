package com.pokit.content.dto.response

import com.pokit.content.model.ReportReason
import io.swagger.v3.oas.annotations.media.Schema

data class ReportReasonResponse(
    @field:Schema(description = "신고 사유 코드", example = "SEXUAL")
    val code: String,
    
    @field:Schema(description = "신고 사유 설명", example = "성적인 링크")
    val description: String
)

fun ReportReason.toResponse() = ReportReasonResponse(
    code = this.name,
    description = this.description
)
