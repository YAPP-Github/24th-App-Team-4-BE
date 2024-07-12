package com.pokit.auth.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleIdTokenResponse(
    @JsonProperty("id_token") val idToken: String,
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("expires_in") val expiresIn: Long,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("refresh_token") val refreshToken: String?
)
