package com.pokit.content.dto.request

import com.pokit.content.model.ReportReason
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class ReportContentRequest(
    @field:NotBlank(message = "신고 사유는 필수입니다.")
    @field:Schema(
        description = "신고 사유 코드 (SEXUAL, VIOLENT, SPAM, HARMFUL, FRAUD 중 하나)",
        example = "SEXUAL"
    )
    val reportReason: String,
)
