package com.pokit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class BatchApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application-out-web, application-core, application-out-persistence")
    runApplication<BatchApplication>(*args)
}
