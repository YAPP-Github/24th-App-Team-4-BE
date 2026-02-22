package com.pokit.notification.model

object DeepLinkBuilder {
    private const val SCHEME = "pokit"
    private const val HOST = "shared"

    fun forContent(categoryId: Long, contentId: Long): String =
        "$SCHEME://$HOST?categoryId=$categoryId&contentId=$contentId"

    fun forCategory(categoryId: Long, userId: Long? = null): String {
        val base = "$SCHEME://$HOST?categoryId=$categoryId"
        return if (userId != null) "$base&userId=$userId" else base
    }
}
