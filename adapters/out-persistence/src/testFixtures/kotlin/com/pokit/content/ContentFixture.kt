package com.pokit.content

import com.pokit.content.model.Content
import com.pokit.content.model.ContentType

class ContentFixture {
    companion object {
        fun getContent() = Content(
            categoryId = 1L,
            type = ContentType.LINK,
            data = "blahblah.com",
            title = "어떤 제목",
            memo = "이러한 내용 요약",
            alertYn = "YES"
        )
    }
}
