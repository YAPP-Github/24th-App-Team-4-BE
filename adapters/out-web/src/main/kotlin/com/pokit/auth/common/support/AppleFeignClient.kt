package com.pokit.auth.common.support

import com.pokit.auth.common.config.OpenFeignConfig
import com.pokit.auth.common.dto.ApplePublicKeys
import com.pokit.auth.common.dto.AppleRevokeRequest
import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody


@FeignClient(
    name = "appleClient",
    url = "https://appleid.apple.com/auth",
    configuration = [OpenFeignConfig::class]
)
interface AppleFeignClient {
    @GetMapping("/keys")
    fun getApplePublicKeys(): ApplePublicKeys

    @PostMapping("/revoke", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun revoke(
        @RequestBody appleRevokeRequest: AppleRevokeRequest
    ): Response

}
