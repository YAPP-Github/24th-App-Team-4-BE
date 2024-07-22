package com.pokit.content

import com.pokit.auth.model.PrincipalUser
import com.pokit.auth.model.toDomain
import com.pokit.content.dto.response.BookMarkContentResponse
import com.pokit.content.port.`in`.ContentUseCase
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/content/")
class ContentController(
    private val contentUseCase: ContentUseCase
) {
    @PostMapping("/{contentId}/bookmark")
    fun bookmarkContent(
        @AuthenticationPrincipal principalUser: PrincipalUser,
        @PathVariable("contentId") contentId: Long
    ): ResponseEntity<BookMarkContentResponse> {
        val user = principalUser.toDomain()
        val response = contentUseCase.bookmarkContent(user, contentId = contentId)
        return ResponseEntity.ok(response)
    }
}
