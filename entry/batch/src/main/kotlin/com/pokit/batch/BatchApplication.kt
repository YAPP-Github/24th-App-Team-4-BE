package com.pokit.batch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class BatchApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application-out-web, application-core, application-out-persistence, application-entry-batch")
    runApplication<BatchApplication>(*args)
}