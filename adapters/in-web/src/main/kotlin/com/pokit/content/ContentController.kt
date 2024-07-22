package com.pokit.content

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapUnit
import com.pokit.content.dto.request.CreateContentRequest
import com.pokit.content.dto.request.UpdateContentRequest
import com.pokit.content.dto.request.toDto
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.dto.response.ContentResponse
import com.pokit.content.dto.response.toResponse
import com.pokit.content.exception.ContentErrorCode
import com.pokit.content.port.`in`.ContentUseCase
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
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
}
