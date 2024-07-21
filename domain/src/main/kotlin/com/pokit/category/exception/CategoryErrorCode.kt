package com.pokit.category.exception

import com.pokit.common.exception.ErrorCode

enum class CategoryErrorCode (
    override val message: String,
    override val code: String,
): ErrorCode {
    ALREADY_EXISTS_CATEGORY("사용 중인 포킷명입니다.", "C_001"),
    NOT_FOUND_CATEGORY("포킷 정보를 찾을 수 없습니다.", "C_002"),
    UNAVAILABLE_CATEGORY_NAME("사용할 수 없는 포킷명입니다.", "C_003"),
    NOT_FOUND_CATEGORY_IMAGE("포킷 이미지 정보를 찾을 수 없습니다.", "C_004"),
    MAX_CATEGORY_LIMIT_EXCEEDED("최대 30개의 포킷을 생성할 수 있습니다. 포킷을 삭제한 뒤에 추가해주세요.", "C_005")
}
