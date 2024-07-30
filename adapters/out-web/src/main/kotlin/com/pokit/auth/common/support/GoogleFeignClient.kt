package com.pokit.auth.common.support

import com.pokit.auth.common.config.OpenFeignConfig
import com.pokit.auth.common.dto.GoogleTokenResponse
import com.pokit.auth.common.dto.GoogleUserResponse
import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "googleClient",
    url = "https://oauth2.googleapis.com",
    configuration = [OpenFeignConfig::class]
)
interface GoogleFeignClient {
    @GetMapping("/tokeninfo")
    fun getUserInfo(@RequestParam("id_token") idToken: String): GoogleUserResponse

    @PostMapping("/token")
    fun getToken(
        @RequestParam("code") authorizationCode: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("grant_type") grantType: String
    ): GoogleTokenResponse?

    @PostMapping("/revoke")
    fun revoke(@RequestParam("token") accessToken: String): Response
}
