package com.pokit.content

import com.pokit.content.dto.ContentCommand
import com.pokit.content.dto.request.ContentSearchCondition
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

        fun getContent(categoryId: Long) = Content(
            categoryId = categoryId,
            type = ContentType.LINK,
            data = "blahblah.com",
            title = "어떤 제목",
            memo = "이러한 내용 요약",
            alertYn = "YES"
        )

        fun getAnotherContent(categoryId: Long) = Content(
            categoryId = categoryId,
            type = ContentType.LINK,
            data = "asdasd.com",
            title = "제목제목",
            memo = "메모",
            alertYn = "YES"
        )

        fun getContentCommand(categoryId: Long) = ContentCommand(
            data = "www.naver.com",
            title = "네이버",
            categoryId = categoryId,
            memo = "네이버우어",
            alertYn = "YES"
        )

        fun getInvalidContentCommand(categoryId: Long) = ContentCommand(
            data = "www.naver.com",
            title = "네이버",
            categoryId = categoryId,
            memo = "네이버우어",
            alertYn = "YES"
        )

        fun getCondition(categoryId: Long) = ContentSearchCondition(
            categoryId = categoryId,
            isRead = null,
            favorites = null,
            startDate = null,
            endDate = null,
            categoryIds = null
        )
    }
}
