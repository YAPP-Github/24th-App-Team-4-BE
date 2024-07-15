package com.pokit.user.dto

import com.pokit.user.dto.request.SignUpRequest
import com.pokit.user.model.InterestType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ApiSignUpRequest(
    @NotBlank(message = "닉네임은 필수값입니다.")
    val nickName: String,
    @Size(min = 1, message = "최소 하나 이상이어야 합니다.")
    val interests: List<String>
) {
    fun toSignUpRequest(): SignUpRequest {
        val interestTypes = interests.map {
            InterestType.of(it)
        }

        return SignUpRequest(
            nickName = nickName,
            interests = interestTypes
        )
    }
}
