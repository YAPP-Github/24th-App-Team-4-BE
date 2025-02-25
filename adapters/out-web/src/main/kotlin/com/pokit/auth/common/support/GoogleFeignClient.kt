package com.pokit.auth.common.support

import com.pokit.auth.common.dto.GoogleUserResponse
import com.pokit.config.OpenFeignConfig
import feign.Headers
import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
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

    @PostMapping("/revoke", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    @Headers("Content-Type: application/x-www-form-urlencoded")
    fun revoke(@RequestParam("token") refreshToken: String): Response
}
