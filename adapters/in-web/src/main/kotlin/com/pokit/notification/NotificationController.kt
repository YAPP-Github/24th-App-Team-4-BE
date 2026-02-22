package com.pokit.notification

import com.pokit.auth.config.ErrorOperation
import com.pokit.auth.model.PrincipalUser
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.notification.dto.response.NotificationResponse
import com.pokit.notification.dto.response.UnreadCountResponse
import com.pokit.notification.dto.response.toResponse
import com.pokit.notification.exception.NotificationErrorCode
import com.pokit.notification.port.`in`.NotificationUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "Notification API")
@RestController
@RequestMapping("/api/v2/notifications")
class NotificationController(
    private val notificationUseCase: NotificationUseCase
) {
    @GetMapping
    @Operation(summary = "알림 목록 조회 API", description = "사용자의 알림 목록을 최신순으로 페이징 조회합니다. (10개 단위 무한 스크롤)")
    fun getNotifications(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<SliceResponseDto<NotificationResponse>> {
        return notificationUseCase.getNotifications(user.id, pageable)
            .map { it.toResponse() }
            .wrapSlice()
            .wrapOk()
    }

    @GetMapping("/unread-count")
    @Operation(summary = "안읽은 알림 개수 조회 API", description = "배지 표시용 안읽은 알림 개수를 반환합니다.")
    fun getUnreadCount(
        @AuthenticationPrincipal user: PrincipalUser
    ): ResponseEntity<UnreadCountResponse> {
        return UnreadCountResponse(count = notificationUseCase.getUnreadCount(user.id))
            .wrapOk()
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "알림 읽음 처리 API", description = "특정 알림을 읽음 상태로 변경합니다.")
    @ErrorOperation(NotificationErrorCode::class)
    fun markAsRead(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable notificationId: Long
    ): ResponseEntity<Map<String, String>> {
        notificationUseCase.markAsRead(user.id, notificationId)
        return mapOf("message" to "success").wrapOk()
    }

    @DeleteMapping("/{notificationId}")
    @Operation(summary = "알림 삭제 API", description = "특정 알림을 삭제합니다. (소프트 딜리트)")
    @ErrorOperation(NotificationErrorCode::class)
    fun deleteNotification(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable notificationId: Long
    ): ResponseEntity<Map<String, String>> {
        notificationUseCase.deleteNotification(user.id, notificationId)
        return mapOf("message" to "success").wrapOk()
    }
}
