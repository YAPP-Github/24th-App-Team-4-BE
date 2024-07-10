package com.pokit.user.model

import com.pokit.common.exception.ClientValidationException
import com.pokit.user.exception.UserErrorCode
import java.util.regex.Pattern

data class User(
    val id: Long = 0L,
    val email: String,
    val role: Role,
) {
    init {
        val pattern =
            Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$",
            )
        if (!pattern.matcher(this.email).matches()) throw ClientValidationException(UserErrorCode.INVALID_EMAIL)
    }
}
