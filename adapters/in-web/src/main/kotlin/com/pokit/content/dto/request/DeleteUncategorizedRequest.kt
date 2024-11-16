package com.pokit.content.dto.request

data class DeleteUncategorizedRequest(
    val contentId: List<Long>
)

internal fun DeleteUncategorizedRequest.toDto() = this.contentId.map { it }


