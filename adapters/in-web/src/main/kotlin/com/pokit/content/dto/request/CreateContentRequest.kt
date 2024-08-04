package com.pokit.content.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateContentRequest(
    @field:NotBlank(message = "링크는 필수값입니다.")
    val data: String,
    @field:NotBlank(message = "제목은 필수값입니다.")
    @field:Size(min = 1, max = 20, message = "최대 20자까지만 입력 가능합니다.")
    val title: String,
    val categoryId: Int,
    @field:Size(max = 100, message = "최대 100자까지만 입력 가능합니다.")
    val memo: String,
    val alertYn: String
)

internal fun CreateContentRequest.toDto() = ContentCommand(
    data = this.data,
    title = this.title,
    categoryId = this.categoryId.toLong(),
    memo = this.memo,
    alertYn = this.alertYn
)
