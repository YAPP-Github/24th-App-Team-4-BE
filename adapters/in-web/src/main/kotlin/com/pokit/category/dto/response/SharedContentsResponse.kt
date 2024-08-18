package com.pokit.category.dto.response

import com.pokit.category.model.Category
import com.pokit.common.dto.DateFormatters
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.content.dto.response.SharedContentResult
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl

data class SharedContentsResponse(
    var category: SharedCategoryResponse,
    var contents: SliceResponseDto<SharedContentResponse>
) {
    companion object {
        fun from(sharedContents: Slice<SharedContentResult>, category: Category): SharedContentsResponse {
            val sharedContentResponse = sharedContents.content.map { SharedContentResponse.of(it) }
            val contents = SliceImpl(sharedContentResponse, sharedContents.pageable, sharedContents.hasNext())

            return SharedContentsResponse(
                category = SharedCategoryResponse.of(category),
                contents = contents.wrapSlice(),
            )
        }
    }
}

data class SharedContentResponse(
    val contentId: Long,
    val data: String,
    val domain: String,
    val title: String,
    val memo: String,
    val createdAt: String,
    val thumbNail: String,
) {
    companion object {
        fun of(content: SharedContentResult): SharedContentResponse {
            return SharedContentResponse(
                contentId = content.contentId,
                data = content.data,
                domain = content.domain,
                title = content.title,
                memo = content.memo,
                createdAt = content.createdAt.format(DateFormatters.DATE_FORMAT_YYYY_MM_DD),
                thumbNail = content.thumbNail,
            )
        }
    }
}

data class SharedCategoryResponse(
    val categoryId: Long = 0L,
    var categoryName: String,
    var contentCount: Int = 0,
) {
    companion object {
        fun of(category: Category): SharedCategoryResponse {
            return SharedCategoryResponse(
                categoryId = category.categoryId,
                categoryName = category.categoryName,
                contentCount = category.contentCount,
            )
        }
    }
}
