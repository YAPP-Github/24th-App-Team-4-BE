package com.pokit.category.exception

import com.pokit.common.exception.ErrorCode

enum class CategoryErrorCode(
    override val message: String,
    override val code: String,
) : ErrorCode {
    ALREADY_EXISTS_CATEGORY("사용 중인 포킷명입니다.", "CA_001"),
    NOT_FOUND_CATEGORY("포킷 정보를 찾을 수 없습니다.", "CA_002"),
    UNAVAILABLE_CATEGORY_NAME("사용할 수 없는 포킷명입니다.", "CA_003"),
    NOT_FOUND_CATEGORY_IMAGE("포킷 이미지 정보를 찾을 수 없습니다.", "CA_004"),
    MAX_CATEGORY_LIMIT_EXCEEDED("최대 30개의 포킷을 생성할 수 있습니다. 포킷을 삭제한 뒤에 추가해주세요.", "CA_005"),
    NOT_FOUND_UNCATEGORIZED_IMAGE("미분류 카테고리 이미지를 찾는데 실패했습니다.", "CA_006"),
    SHARE_ALREADY_EXISTS_CATEGORY("직접 생성한 포킷은 공유받을 수 없습니다.\n 다른 유저의 포킷을 공유받아보세요.", "CA_007"),
}
