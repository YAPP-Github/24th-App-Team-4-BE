package com.pokit.auth.common.support

import com.pokit.auth.common.config.OpenFeignConfig
import com.pokit.auth.common.dto.ApplePublicKeys
import com.pokit.auth.common.dto.AppleRevokeRequest
import com.pokit.auth.common.dto.AppleTokenResponse
import feign.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam


@FeignClient(
    name = "appleClient",
    url = "https://appleid.apple.com/auth",
    configuration = [OpenFeignConfig::class]
)
interface AppleFeignClient {
    @GetMapping("/keys")
    fun getApplePublicKeys(): ApplePublicKeys

    @PostMapping("/revoke", produces = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun revoke(
        @RequestBody appleRevokeRequest: AppleRevokeRequest
    ): Response

    @PostMapping("/token", produces = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getToken(
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
        @RequestParam("code") code: String,
        @RequestParam("grant_type") grantType: String,
    ): AppleTokenResponse?

}
