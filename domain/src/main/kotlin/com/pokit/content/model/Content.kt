package com.pokit.content.model

import com.pokit.content.dto.ContentCommand

data class Content(
    val id: Long = 0L,
    var categoryId: Long,
    var type: ContentType = ContentType.LINK,
    var data: String,
    var title: String,
    var memo: String,
    var alertYn: String,
) {
    fun modify(contetCommand: ContentCommand){
        this.categoryId = contetCommand.categoryId
        this.data = contetCommand.data
        this.title = contetCommand.title
        this.memo = contetCommand.memo
        this.alertYn = contetCommand.alertYn
    }
}
