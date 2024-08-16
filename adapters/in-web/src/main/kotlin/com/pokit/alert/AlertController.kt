package com.pokit.alert

import com.pokit.alert.dto.response.AlertsResponse
import com.pokit.alert.port.`in`.AlertUseCase
import com.pokit.auth.model.PrincipalUser
import com.pokit.common.dto.SliceResponseDto
import com.pokit.common.wrapper.ResponseWrapper.wrapOk
import com.pokit.common.wrapper.ResponseWrapper.wrapSlice
import com.pokit.common.wrapper.ResponseWrapper.wrapUnit
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/alert")
class AlertController(
    private val alertUseCase: AlertUseCase
) {
    @GetMapping
    fun getAlerts(
        @AuthenticationPrincipal user: PrincipalUser,
        @PageableDefault(
            page = 0,
            size = 10,
            sort = ["createdAt"],
            direction = Sort.Direction.DESC
        ) pageable: Pageable
    ): ResponseEntity<SliceResponseDto<AlertsResponse>> {
        return alertUseCase.getAlerts(user.id, pageable)
            .wrapSlice()
            .wrapOk()
    }

    @PutMapping("/{alertId}")
    fun deleteAlert(
        @AuthenticationPrincipal user: PrincipalUser,
        @PathVariable("alertId") alertId: Long
    ): ResponseEntity<Unit> {
        return alertUseCase.delete(user.id, alertId)
            .wrapUnit()
    }
}
