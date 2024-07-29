package com.pokit.user.dto.response

data class CheckDuplicateNicknameResponse(
    val isDuplicate: Boolean
)

fun Boolean.toResponse() = CheckDuplicateNicknameResponse(
    isDuplicate = this
)
