package com.pokit.content.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

data class UpdateThumbnailRequest(
    @field:Schema(description = "수정할 썸네일 링크")
    @field:NotBlank
    val thumbnail: String,
)

internal fun UpdateThumbnailRequest.toDto() = thumbnail
