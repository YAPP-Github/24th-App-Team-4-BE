package com.pokit.category.v2.dto.request

import com.pokit.category.dto.CategoryCommand
import com.pokit.category.model.OpenType
import com.pokit.user.model.InterestType
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCategoryRequestV2(
    @field:NotBlank(message = "포킷 명은 필수 값입니다.")
    @field:Size(min = 1, max = 10, message = "최대 10자까지 입력 가능합니다.")
    val categoryName: String,
    val categoryImageId: Int,
    @field:Schema(description = "PUBLIC / PRIVATE 중 하나여야 합니다.")
    val openType: String,
    @field:Schema(description = "관심사 리스트랑 같은 리스트 쓰니 그 중 하나 선택")
    val keywordType: String = "default",
    @field:Schema(description = "해당 카테고리의 알림 수신 여부")
    val alertEnabled: Boolean? = null,
)

internal fun CreateCategoryRequestV2.toDto() = CategoryCommand(
    categoryName = this.categoryName,
    categoryImageId = this.categoryImageId,
    openType = OpenType.of(this.openType),
    keywordType = InterestType.of(this.keywordType),
    alertEnabled = this.alertEnabled,
)
