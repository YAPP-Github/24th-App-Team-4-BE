package com.pokit.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateProfileRequest(
    val profileImageId: Int,
    @field:NotBlank(message = "닉네임은 필수값입니다.")
    @field:Size(max = 10, message = "닉네임은 10자 이하만 가능합니다.")
    val nickname: String
)

internal fun UpdateProfileRequest.toDto() = UserCommand(
    profileImageId = this.profileImageId,
    nickname = this.nickname
)
