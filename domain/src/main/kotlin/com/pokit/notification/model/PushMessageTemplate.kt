package com.pokit.notification.model

data class PushMessageTemplate(
    val id: Long = 0L,
    val notificationType: NotificationType,
    val title: String,
    val body: String,
    val navigationType: NavigationType,
) {
    fun toNotification(
        userId: Long,
        categoryName: String? = null,
        nickname: String? = null,
        categoryImageUrl: String? = null,
        deepLink: String? = null,
    ) = Notification(
        userId = userId,
        notificationType = notificationType,
        title = title.resolve(categoryName, nickname),
        body = body.resolve(categoryName, nickname),
        categoryImageUrl = categoryImageUrl,
        navigationType = navigationType,
        deepLink = deepLink,
    )
}

private fun String.resolve(categoryName: String?, nickname: String?): String {
    var result = this
    categoryName?.let { result = result.replace("{categoryName}", it) }
    nickname?.let { result = result.replace("{nickname}", it) }
    return result
}
