package com.pokit.category.v2

import com.pokit.auth.model.PrincipalUser
import com.pokit.category.port.`in`.CategoryUseCase
import com.pokit.category.v2.dto.request.DuplicateCategoryRequestV2
import com.pokit.category.v2.dto.request.toDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "포킷 공유 관련 API Ver2")
@RestController
@RequestMapping("/api/v2/category/share")
class CategoryShareControllerV2(
    private val categoryUseCase: CategoryUseCase,
) {
    @Operation(summary = "포킷 복제 API")
    @PostMapping
    fun duplicateCategory(
        @AuthenticationPrincipal user: PrincipalUser,
        @RequestBody request: DuplicateCategoryRequestV2,
    ): ResponseEntity<Unit> =
        categoryUseCase.duplicateCategoryV2(user.id, request.toDto())
            .wrapOk()
}
