package com.pokitmonz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.pokitmonz"])
class PokitApplication

fun main(args: Array<String>) {
    runApplication<PokitApplication>(*args)
}
