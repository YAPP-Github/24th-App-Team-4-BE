package com.pokit.user.dto.request

import com.pokit.user.model.InterestType

data class SignUpRequest(
    val nickName: String,
    val interests: List<InterestType>
) {
}
