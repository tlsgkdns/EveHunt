package com.evehunt.evehunt.global.infra.web

import com.evehunt.evehunt.global.infra.converter.SearchTypeConverter
import com.evehunt.evehunt.global.infra.converter.SortTypeConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    @Override
    override fun addCorsMappings(registry: CorsRegistry)
    {
        registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE")
            .allowedHeaders("Authorization", "Content-Type", "Access-Control-Allow-Origin")
            .exposedHeaders("Custom-Header")
            .allowCredentials(true)
            .maxAge(3600)
    }

    @Override
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(SearchTypeConverter())
        registry.addConverter(SortTypeConverter())
    }
}