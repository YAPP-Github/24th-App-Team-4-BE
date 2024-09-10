package com.pokit.version

import com.pokit.version.dto.VersionCheckResponse
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/version")
class VersionController {

    companion object {
        private const val RECENT_VERSION = "1.0.1"
    }

    @GetMapping
    @Operation(summary = "최신 버전 체크 api")
    fun checkVersion() = ResponseEntity.ok(VersionCheckResponse(RECENT_VERSION))
}
