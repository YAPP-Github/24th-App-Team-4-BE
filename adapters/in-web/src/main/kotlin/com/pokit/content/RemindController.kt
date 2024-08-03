package com.pokit.content

import com.pokit.auth.model.PrincipalUser
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.content.dto.response.RemindContentResponse
import com.pokit.content.dto.response.toResponse
import com.pokit.content.port.`in`.ContentUseCase
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/remind")
class RemindController(
    private val contentUseCase: ContentUseCase
) {
    @GetMapping("/bookmark")
    @Operation(summary = "즐겨찾기 링크 모음 조회 API")
    fun getBookmarkContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
    ): ResponseEntity<SliceResponseDto<RemindContentResponse>> =
        contentUseCase.getBookmarkContents(user.id, pageable)
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()
}
