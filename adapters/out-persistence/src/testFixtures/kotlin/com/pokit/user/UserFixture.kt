package com.pokit.user

import com.pokit.user.dto.UserInfo
import com.pokit.user.model.Role
import com.pokit.user.model.User

class UserFixture {
    companion object {
        fun getUser() = User(1L, "ij@naver.com", Role.USER)

        fun getUserInfo() = UserInfo("ig@naver.com")
    }
}
