package com.pokit.user.dto.request

import com.pokit.user.model.InterestType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ApiSignUpRequest(
    @NotBlank(message = "닉네임은 필수값입니다.")
    @Size(max = 10, message = "닉네임은 10자 이하만 가능합니다.")
    val nickName: String,
    @Size(min = 1, max = 3, message = "최소 하나 이상, 세개 이하만 가능합니다.")
    val interests: List<String>
)

internal fun ApiSignUpRequest.toDto(): SignUpRequest {
    val interestTypes = interests.map { InterestType.of(it) }
    return SignUpRequest(
        nickName = this.nickName,
        interests = interestTypes
    )
}
