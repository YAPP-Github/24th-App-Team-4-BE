package com.pokit.user.dto.request

import jakarta.validation.constraints.Size

data class UpdateInterestRequest(
    @Size(min = 1, max = 3, message = "최소 하나 이상, 세개 이하만 가능합니다.")
    val interests: List<String>,
)
