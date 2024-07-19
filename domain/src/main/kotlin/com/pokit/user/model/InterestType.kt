package com.pokit.user.model

import com.pokit.common.exception.ClientValidationException
import com.pokit.user.exception.UserErrorCode

enum class InterestType(
    val kor: String,
) {
    SPORTS("스포츠/레저");

    companion object {
        fun of(input: String): InterestType {
            return InterestType.entries
                .firstOrNull { it.kor == input }
                ?: throw ClientValidationException(UserErrorCode.INVALID_INTEREST_TYPE)
        }
    }
}
