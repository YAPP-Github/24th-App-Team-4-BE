package com.pokit.category.v1

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.category.dto.CategoriesResponse
import com.pokit.category.v1.dto.request.CreateCategoryRequest
import com.pokit.category.v1.dto.request.toDto
import com.pokit.category.v1.dto.response.CategoryCountResponse
import com.pokit.category.v1.dto.response.CategoryResponse
import com.pokit.category.v1.dto.response.toResponse
import com.pokit.category.exception.CategoryErrorCode
import com.pokit.category.model.CategoryImage
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.common.wrapper.ResponseWrapper.wrapUnit
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/category")
class CategoryController(
    private val categoryUseCase: CategoryUseCase
) {
    @Operation(summary = "포킷 생성 API")
    @ErrorOperation(CategoryErrorCode::class)
    @PostMapping
    fun createCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @Valid @RequestBody request: CreateCategoryRequest
    ): ResponseEntity<CategoryResponse> =
        categoryUseCase.create(request.toDto(), user.id)
            .toResponse()
            .wrapOk()

    @Operation(summary = "포킷 목록 조회 API")
    @GetMapping
    fun getCategories(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
        @RequestParam(defaultValue = "true") filterUncategorized: Boolean
    ): ResponseEntity<SliceResponseDto<CategoriesResponse>> =
        categoryUseCase.getCategories(user.id, pageable, filterUncategorized)
            .wrapSlice()
            .wrapOk()

    @Operation(summary = "포킷 상세 조회 API")
    @GetMapping("/{categoryId}")
    fun getCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable categoryId: Long,
    ): ResponseEntity<CategoryResponse> =
        categoryUseCase.getCategory(user.id, categoryId)
            .toResponse()
            .wrapOk()

    @Operation(summary = "포킷 수정 API")
    @ErrorOperation(CategoryErrorCode::class)
    @PatchMapping("/{categoryId}")
    fun updateCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @Valid @RequestBody request: CreateCategoryRequest,
        @PathVariable categoryId: Long,
    ): ResponseEntity<CategoryResponse> =
        categoryUseCase.update(request.toDto(), user.id, categoryId)
            .toResponse()
            .wrapOk()

    @Operation(summary = "포킷 삭제 API")
    @ErrorOperation(CategoryErrorCode::class)
    @PutMapping("/{categoryId}")
    fun deleteCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable categoryId: Long,
    ): ResponseEntity<Unit> =
        categoryUseCase.delete(categoryId, user.id)
            .wrapUnit()

    @Operation(summary = "유저의 포킷 개수 조회 API")
    @GetMapping("/count")
    fun getTotalCount(
        @AuthenticationPrincipal user: PrincipalUser,
    ): ResponseEntity<CategoryCountResponse> {
        val count = categoryUseCase.getTotalCount(user.id)
        return CategoryCountResponse(count)
            .wrapOk()
    }

    @Operation(summary = "포킷 프로필 목록 조회 API")
    @GetMapping("/images")
    fun getCategoryImages(): ResponseEntity<List<CategoryImage>> =
        categoryUseCase.getAllCategoryImages()
            .wrapOk()

}
