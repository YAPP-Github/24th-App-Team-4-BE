package com.pokit.user.model

import com.pokit.common.exception.ClientValidationException
import com.pokit.user.exception.UserErrorCode

enum class InterestType(
    val kor: String,
) {
    SPORTS("스포츠/레저"),
    OFFICE("문구/오피스"),
    FASHION("패션"),
    TRAVEL("여행"),
    ECONOMY("경제/시사"),
    MOVIE_DRAMA("영화/드라마"),
    RESTAURANT("맛집"),
    INTERIOR("인테리어"),
    IT("IT"),
    DESIGN("디자인"),
    SELF_IMPROVEMENT("자기계발"),
    HUMOR("유머"),
    MUSIC("음악"),
    JOB_INFO("취업정보")
    ;

    companion object {
        fun of(input: String): InterestType {
            return InterestType.entries
                .firstOrNull { it.kor == input }
                ?: throw ClientValidationException(UserErrorCode.INVALID_INTEREST_TYPE)
        }
    }
}
