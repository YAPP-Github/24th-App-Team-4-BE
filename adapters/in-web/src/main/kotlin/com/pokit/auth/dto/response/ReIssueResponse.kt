package com.pokit.auth.dto.response

data class ReIssueResponse(
    val accessToken: String
)

internal fun String.toReIssueResponse() = ReIssueResponse(
    accessToken = this
)
