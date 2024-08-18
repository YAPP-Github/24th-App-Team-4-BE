package com.pokit.category.dto.response

import com.pokit.category.model.Category
import com.pokit.common.dto.SliceResponseDto
import com.pokit.content.dto.response.SharedContentResult

data class SharedContentsResponse(
    var category: SharedCategoryResponse,
    var contents: SliceResponseDto<SharedContentResult>
) {
    companion object {
        fun from(sharedContents: SliceResponseDto<SharedContentResult>, category: Category): SharedContentsResponse {
            return SharedContentsResponse(
                category = SharedCategoryResponse.of(category),
                contents = sharedContents,
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
