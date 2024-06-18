package com.evehunt.evehunt.global.infra.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

private const val SECURITY_SCHEMA_NAME = "authorization"
@Configuration
class SwaggerConfig {
    @Bean
    fun openAPI(): OpenAPI = OpenAPI()
        .components(
            Components().addSecuritySchemes(
                SECURITY_SCHEMA_NAME, SecurityScheme()
                    .name(SECURITY_SCHEMA_NAME)
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")))
        .addSecurityItem(SecurityRequirement().addList(SECURITY_SCHEMA_NAME))
        .info(
            Info()
                .title("RICU")
                .description("Welcome to RICU Schema")
                .version("1.0.0")
        )
}