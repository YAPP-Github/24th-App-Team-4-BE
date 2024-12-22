package com.pokit.user.dto.response

import com.pokit.user.model.User
import com.pokit.user.model.UserImage

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
)

data class InvitedUserResponse(
    val userId: Long,
    val nickname: String,
    val profileImage: UserImage?,
)

fun User.toResponse() = UserResponse(
    id = this.id,
    email = this.email,
    nickname = this.nickName,
)

fun List<User>.toResponse(): List<InvitedUserResponse> {
    return this.map {
        InvitedUserResponse(
            userId = it.id,
            nickname = it.nickName,
            profileImage = it.image
        )
    }
}
