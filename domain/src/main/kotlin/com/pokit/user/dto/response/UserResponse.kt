package com.pokit.user.dto.response

import com.pokit.category.model.CategoryImage
import com.pokit.user.model.User

data class UserResponse (
    val id: Long,
    val email: String,
    val nickname: String,
    val profileImage: CategoryImage?
)

fun User.toResponse() = UserResponse(
    id = this.id,
    email = this.email,
    nickname = this.nickName,
    profileImage = this.profileImage
)
