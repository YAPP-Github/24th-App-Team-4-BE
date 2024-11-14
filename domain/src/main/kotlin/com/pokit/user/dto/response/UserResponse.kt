package com.pokit.user.dto.response

import com.pokit.user.model.User

data class UserResponse (
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImage: String?
)

fun User.toResponse() = UserResponse(
    id = this.id,
    email = this.email,
    nickname = this.nickName,
    profileImage = this.profileImage
)
