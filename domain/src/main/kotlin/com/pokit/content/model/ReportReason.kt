package com.pokit.content.model

import com.pokit.common.exception.ClientValidationException
import com.pokit.content.exception.ContentErrorCode

enum class ReportReason(val description: String) {
    SEXUAL("성적인 링크"),
    VIOLENT("폭력적 또는 혐오스러운 링크"),
    HATE("증오 또는 학대하는 링크"),
    HARMFUL("유해하거나 위험한 링크"),
    FRAUD("스팸 또는 혼돈을 야기하는 링크");

    companion object {
        fun of(value: String): ReportReason {
            return entries.find { it.name.equals(value, ignoreCase = true) }
                ?: throw ClientValidationException(ContentErrorCode.INVALID_REPORT_REASON)
        }
    }
}
