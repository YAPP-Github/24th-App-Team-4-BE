package com.pokitmonz.pokit.common.exception

interface ErrorCode {
    fun getMessage(): String

    fun getCode(): String
}
