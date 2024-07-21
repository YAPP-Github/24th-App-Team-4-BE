package com.pokit.common.wrapper

import com.pokit.common.dto.SliceResponseDto
import org.springframework.data.domain.Slice
import org.springframework.http.ResponseEntity

object ResponseWrapper {
    fun <T> T.wrapOk(): ResponseEntity<T> = ResponseEntity.ok(this)

    fun <T> Slice<T>.wrapSlice() = SliceResponseDto(this)

    fun Unit.wrapUnit(): ResponseEntity<Unit> = ResponseEntity.noContent().build()
}
