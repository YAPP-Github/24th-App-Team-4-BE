package com.pokit.content

import com.pokit.auth.model.PrincipalUser
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.content.dto.response.BookmarkCountResponse
import com.pokit.content.dto.response.RemindContentResponse
import com.pokit.content.dto.response.UnreadCountResponse
import com.pokit.content.dto.response.toResponse
import com.pokit.content.port.`in`.ContentUseCase
import com.pokit.content.port.`in`.DailyContentUseCase
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
    private val contentUseCase: ContentUseCase,
    private val dailyContentUseCase: DailyContentUseCase,
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

    @GetMapping("/unread")
    @Operation(summary = "읽지 않음 컨텐츠 조회 API")
    fun getUnreadContents(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable,
    ): ResponseEntity<SliceResponseDto<RemindContentResponse>> =
        contentUseCase.getUnreadContents(user.id, pageable)
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()

    @GetMapping("/today")
    @Operation(summary = "오늘의 리마인드 조회 API")
    fun getDailyContents(
        @AuthenticationPrincipal user: PrincipalUser,
    ): ResponseEntity<List<RemindContentResponse>> =
        dailyContentUseCase.getDailyContents(user.id)
            .map { it.toResponse() }
            .wrapOk()

    @GetMapping("/unread/count")
    @Operation(summary = "안읽음 컨텐츠 개수 조회 API")
    fun getUnreadCount(
        @AuthenticationPrincipal user: PrincipalUser
    ): ResponseEntity<UnreadCountResponse> {
        val unreadCount = contentUseCase.getUnreadCount(user.id)
        return UnreadCountResponse(unreadCount)
            .wrapOk()
    }

    @GetMapping("/bookmark/count")
    @Operation(summary = "즐겨찾기 컨텐츠 개수 조회 API")
    fun getBookmarkCount(
        @AuthenticationPrincipal user: PrincipalUser
    ): ResponseEntity<BookmarkCountResponse> {
        val bookmarkCount = contentUseCase.getBookmarkCount(user.id)
        return BookmarkCountResponse(bookmarkCount)
            .wrapOk()
    }
}
