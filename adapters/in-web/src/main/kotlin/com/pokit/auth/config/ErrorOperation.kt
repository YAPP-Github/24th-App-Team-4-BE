package com.pokit.auth.config

import com.pokit.common.exception.ErrorCode
import kotlin.reflect.KClass

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class ErrorOperation(val value: KClass<out ErrorCode>)
