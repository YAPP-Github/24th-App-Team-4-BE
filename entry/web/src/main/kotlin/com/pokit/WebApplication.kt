package com.pokit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WebApplication

fun main(args: Array<String>) {
    System.setProperty("spring.config.name", "application-out-web, application-core, application-out-persistence, application-in-web")
    runApplication<WebApplication>(*args)
}
