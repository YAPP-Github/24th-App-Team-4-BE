package com.pokit

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class WebApplication

fun main(args: Array<String>) {
    runApplication<WebApplication>(*args)
}
