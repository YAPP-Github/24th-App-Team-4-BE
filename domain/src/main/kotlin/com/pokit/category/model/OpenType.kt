package com.pokit.category.model

import com.pokit.category.exception.CategoryErrorCode
import com.pokit.common.exception.ClientValidationException

enum class OpenType {
    PUBLIC,
    PRIVATE
    ;

    companion object {
        fun of(input: String): OpenType {
            return OpenType.entries
                .firstOrNull { it.toString() == input }
                ?: throw ClientValidationException(CategoryErrorCode.INVALID_OPENTYPE)
        }
    }
}
