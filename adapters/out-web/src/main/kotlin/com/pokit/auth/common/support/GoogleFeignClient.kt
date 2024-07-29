package com.pokit.auth.common.support

import com.pokit.auth.common.config.OpenFeignConfig
import com.pokit.auth.common.dto.GoogleUserResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "googleClient",
    url = "https://oauth2.googleapis.com",
    configuration = [OpenFeignConfig::class]
)
interface GoogleFeignClient {
    @GetMapping("/tokeninfo")
    fun getUserInfo(@RequestParam("id_token") idToken: String): GoogleUserResponse
}
