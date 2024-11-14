package com.pokit.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class UpdateProfileRequest(
    @field:NotNull(message = "이미지 ID는 필수값입니다.")
    val profileImageId: Int,
    @field:NotBlank(message = "닉네임은 필수값입니다.")
    @field:Size(max = 10, message = "닉네임은 10자 이하만 가능합니다.")
    val nickname: String
)

internal fun UpdateProfileRequest.toDto() = UserCommand(
    profileImageId = this.profileImageId,
    nickname = this.nickname
)
