package com.pokit.auth.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.media.Schema
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSchemas(
                        "ErrorResponse", Schema<Any>()
                            .addProperty("message", Schema<Any>().type("string"))
                            .addProperty("code", Schema<Any>().type("string"))
                    )
            )
            .info(configurationInfo())
    }

    private fun configurationInfo(): Info {
        return Info()
            .title("POKIT API")
            .description("포킷 api 명세서")
            .version("1.0")
    }
}
