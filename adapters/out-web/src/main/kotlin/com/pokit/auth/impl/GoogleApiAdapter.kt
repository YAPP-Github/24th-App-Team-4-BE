package com.pokit.auth.impl

import com.pokit.auth.common.support.GoogleFeignClient
import com.pokit.auth.port.out.GoogleApiClient
import com.pokit.token.model.AuthPlatform
import com.pokit.user.dto.UserInfo
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component

@Component
class GoogleApiAdapter(
    private val googleFeignClient: GoogleFeignClient
) : GoogleApiClient {
    override fun getUserInfo(idToken: String): UserInfo {
        val response = googleFeignClient.getUserInfo(idToken)

        return UserInfo(
            email = response.email,
            authPlatform = AuthPlatform.GOOGLE
        )
    }
}
