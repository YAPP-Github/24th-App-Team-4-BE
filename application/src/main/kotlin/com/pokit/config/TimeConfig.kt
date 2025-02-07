package com.pokit.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.function.Supplier

@Configuration
class TimeConfig {

    @Bean("nowTimeSupplier")
    fun nowTimeSupplier(): Supplier<LocalDateTime> {
        return Supplier { LocalDateTime.now() }
    }

    @Bean("nowDateSupplier")
    fun nowDateSupplier(): Supplier<LocalDate> {
        return Supplier { LocalDate.now() }
    }
}
