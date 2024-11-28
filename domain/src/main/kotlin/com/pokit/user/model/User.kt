package com.pokit.user.model

import com.pokit.common.exception.ClientValidationException
import com.pokit.token.model.AuthPlatform
import com.pokit.user.exception.UserErrorCode
import java.util.regex.Pattern

data class User(
    val id: Long = 0L,
    val email: String = "EMAIL@EMAIL.COM",
    val role: Role,
    var nickName: String = "NOT_REGISTERED",
    val authPlatform: AuthPlatform,
    var registered: Boolean = false,
    var sub: String?,
    var image: UserImage? = null
) {
    fun register(nickName: String) {
        this.nickName = nickName
        this.registered = true
    }

    fun insertSub(sub: String) {
        this.sub = sub
    }

    fun modifyNickname(nickName: String) {
        this.nickName = nickName
    }

    fun modifyProfile(image: UserImage, nickname: String) {
        this.image = image
        this.nickName = nickname
    }

    init {
        val pattern =
            Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            )
        if (!pattern.matcher(this.email).matches()) throw ClientValidationException(UserErrorCode.INVALID_EMAIL)
    }
}
