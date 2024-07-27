package com.pokit.auth.common.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class AppleRevokeRequest(
    @JsonProperty("client_id")
    val clientId: String,
    @JsonProperty("client_secret")
    val clientSecret: String,
    val token: String,
    @JsonProperty("token_type_hint")
    val tokenTypeHint: String
)
