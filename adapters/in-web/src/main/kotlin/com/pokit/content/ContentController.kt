package com.pokit.content

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.CategoryStatus
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.common.wrapper.ResponseWrapper.wrapUnit
import com.pokit.content.dto.request.*
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.dto.response.ContentResponse
import com.pokit.content.dto.response.ContentsResponse
import com.pokit.content.dto.response.toResponse
import com.pokit.content.exception.ContentErrorCode
import com.pokit.content.model.Content
import com.pokit.content.port.`in`.ContentUseCase
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/content")
class ContentController(
    private val contentUseCase: ContentUseCase
) {
    @PostMapping
    @Operation(summary = "컨텐츠 추가 API")
    @ErrorOperation(CategoryErrorCode::class)
    fun createContent(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @Valid @RequestBody request: CreateContentRequest
    ): ResponseEntity<ContentResponse> {
        val user = principalUser.toDomain()
        return contentUseCase.create(user, request.toDto())
            .toResponse()
            .wrapOk()
    }

    @PatchMapping("/{contentId}")
    @Operation(summary = "컨텐츠 수정 API")
    @ErrorOperation(ContentErrorCode::class)
    fun updateContent(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("contentId") contentId: Long,
        @Valid @RequestBody request: UpdateContentRequest
    ): ResponseEntity<ContentResponse> {
        val user = principalUser.toDomain()
        return contentUseCase.update(user, request.toDto(), contentId)
            .toResponse()
            .wrapOk()
    }

    @PutMapping("/{contentId}")
    @Operation(summary = "컨텐츠 삭제 API")
    @ErrorOperation(ContentErrorCode::class)
    fun deleteContent(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("contentId") contentId: Long
    ): ResponseEntity<Unit> {
        val user = principalUser.toDomain()
        return contentUseCase.delete(user, contentId)
            .wrapUnit()
    }


    @PostMapping("/{contentId}/bookmark")
    @Operation(summary = "즐겨찾기 API")
    @ErrorOperation(ContentErrorCode::class)
    fun bookmarkContent(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("contentId") contentId: Long
    ): ResponseEntity<BookMarkContentResponse> {
        val user = principalUser.toDomain()
        val response = contentUseCase.bookmarkContent(user, contentId = contentId)
        return ResponseEntity.ok(response)
    }

    @PutMapping("/{contentId}/bookmark")
    @Operation(summary = "즐겨찾기 취소 API")
    fun cancelBookmark(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("contentId") contentId: Long
    ): ResponseEntity<Unit> {
        val user = principalUser.toDomain()
        return contentUseCase.cancelBookmark(user, contentId)
            .wrapUnit()
    }

    @GetMapping("/{categoryId}")
    @Operation(summary = "카테고리 내 컨텐츠 목록 조회")
    fun getContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("categoryId") categoryId: Long,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        condition: ContentSearchParams,
    ): ResponseEntity<SliceResponseDto<ContentsResponse>> {
        return contentUseCase.getContents(
            user.id,
            condition.copy(categoryId = categoryId).toDto(),
            pageable
        )
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()
    }

    @GetMapping("/uncategorized")
    @Operation(summary = "미분류 카테고리 컨텐츠 조회")
    fun getUncategorizedContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
    ): ResponseEntity<SliceResponseDto<ContentsResponse>> {
        return contentUseCase.getContentsByCategoryName(
            user.id,
            CategoryStatus.UNCATEGORIZED.displayName,
            pageable
        )
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()
    }

    @PostMapping("/{contentId}")
    @Operation(summary = "컨텐츠 상세조회 API")
    fun getContent(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("contentId") contentId: Long
    ): ResponseEntity<ContentResponse> {
        return contentUseCase.getContent(user.id, contentId)
            .toResponse()
            .wrapOk()
    }

    @GetMapping
    @Operation(summary = "컨텐츠 검색 API")
    fun searchContent(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        condition: ContentSearchParams,
    ): ResponseEntity<SliceResponseDto<ContentsResponse>> {
        return contentUseCase.getContents(
            user.id,
            condition.toDto(),
            pageable
        )
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()
    }

    @PutMapping("/uncategorized")
    @Operation(summary = "미분류 링크 삭제 API")
    @ErrorOperation(ContentErrorCode::class)
    fun deleteUncategorizedContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: DeleteUncategorizedRequest
    ): ResponseEntity<Unit> {
        return contentUseCase.deleteUncategorized(user.id, request.toDto())
            .wrapUnit()
    }

    @PatchMapping
    @Operation(summary = "미분류 링크 포킷으로 이동 API")
    fun categorizeContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: CategorizeRequest
    ): ResponseEntity<Unit> {
        return contentUseCase.categorize(user.id, request.toDto())
            .wrapUnit()
    }

    @PatchMapping("/thumbnail/{contentId}")
    @Operation(summary = "썸네일 수정 API")
    fun updateThumbnail(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("contentId") contentId: Long,
        @RequestBody request: UpdateThumbnailRequest
    ): ResponseEntity<Content> {
        return contentUseCase.updateThumbnail(user.id, contentId, request.toDto())
            .wrapOk()
    }

    @GetMapping("/recommended")
    @Operation(summary = "추천 컨텐츠 목록 조회 API", description = "keyword 생략이나 비워서 보내면 전체보기 적용")
    fun getRecommendedContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @RequestParam("keyword") keyword: String?,
    ): ResponseEntity<SliceResponseDto<ContentsResponse>> {
        return contentUseCase.getRecommendedContent(user.id, keyword, pageable)
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()

    }

}

