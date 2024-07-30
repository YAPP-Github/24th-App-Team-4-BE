package com.pokit.content

import com.pokit.auth.model.PrincipalUser
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.content.dto.response.RemindContentResponse
import com.pokit.content.dto.response.toResponse
import com.pokit.content.port.`in`.ContentUseCase
import io.swagger.v3.oas.annotations.Operation
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
        @AuthenticationPrincipal user: PrincipalUser
    ): ResponseEntity<List<RemindContentResponse>> =
        contentUseCase.getBookmarkContents(user.id)
            .map { it.toResponse() }
            .wrapOk()
}
