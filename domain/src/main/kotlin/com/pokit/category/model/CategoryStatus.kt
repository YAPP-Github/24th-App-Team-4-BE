package com.pokit.category.model

enum class CategoryStatus(
    val displayName: String
) {
    UNCATEGORIZED("미분류"),
    FAVORITE("즐겨찾기")
    ;

    companion object {
        fun resolveDisplayName(status: String): String =
            entries.find { it.name == status }
                ?.displayName
                ?: status

    }
}
