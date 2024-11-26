package com.pokit.category

import com.pokit.auth.aop.KakaoAuth
import com.pokit.auth.model.PrincipalUser
import com.pokit.category.dto.request.DuplicateCategoryRequest
import com.pokit.category.dto.response.SharedContentsResponse
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapUnit
import com.pokit.content.port.`in`.ContentUseCase
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/category/share")
class CategoryShareController(
    private val categoryUseCase: CategoryUseCase,
    private val contentUseCase: ContentUseCase,
) {
    @KakaoAuth
    @Operation(summary = "포킷 공유 후 callback API")
    @GetMapping("/callback")
    fun completeShare(
        @RequestParam("categoryId") categoryId: Long,
    ): ResponseEntity<Unit> {
        return categoryUseCase.completeShare(categoryId)
            .wrapOk()
    }

    @Operation(summary = "포킷 공유 시 포킷 내 컨텐츠 미리보기 API")
    @GetMapping("/{categoryId}")
    fun getSharedContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable categoryId: Long,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
    ): ResponseEntity<SharedContentsResponse> {
        val category = categoryUseCase.getSharedCategory(categoryId, user.id)
        val content = contentUseCase.getSharedContents(categoryId, pageable)
        return SharedContentsResponse
            .from(content, category)
            .wrapOk()
    }

    @Operation(summary = "포킷 복제 API")
    @PostMapping
    fun duplicateCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: DuplicateCategoryRequest,
    ): ResponseEntity<Unit> =
        categoryUseCase.duplicateCategory(
            request.originCategoryId,
            request.categoryName,
            user.id,
            request.categoryImageId
        )
            .wrapOk()

    @Operation(summary = "포킷 초대 수락 API")
    @PostMapping("/accept/{categoryId}")
    fun acceptCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable categoryId: Long,
    ): ResponseEntity<Unit> {
        return categoryUseCase.acceptCategory(user.id, categoryId)
            .wrapUnit()
    }

}
