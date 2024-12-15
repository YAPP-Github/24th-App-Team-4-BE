package com.pokit.category.v2

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.category.v1.dto.response.CategoryResponse
import com.pokit.category.v1.dto.response.toResponse
import com.pokit.category.v2.dto.request.CreateCategoryRequestV2
import com.pokit.category.v2.dto.request.toDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "Category API Ver2")
@RestController
@RequestMapping("/api/v2/category")
class CategoryControllerV2(
    private val categoryUseCase: CategoryUseCase
) {
    @Operation(summary = "포킷 생성 API Ver2")
    @ErrorOperation(CategoryErrorCode::class)
    @PostMapping
    fun createCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @Valid @RequestBody request: CreateCategoryRequestV2
    ): ResponseEntity<CategoryResponse> {
        return categoryUseCase.create(request.toDto(), user.id)
            .toResponse()
            .wrapOk()
    }

    @Operation(summary = "포킷 추가 API Ver2")
    @ErrorOperation(CategoryErrorCode::class)
    @PatchMapping("/{categoryId}")
    fun updateCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @Valid @RequestBody request: CreateCategoryRequestV2,
        @PathVariable categoryId: Long,
    ): ResponseEntity<CategoryResponse> {
        return categoryUseCase.update(request.toDto(), user.id, categoryId)
            .toResponse()
            .wrapOk()
    }

}
