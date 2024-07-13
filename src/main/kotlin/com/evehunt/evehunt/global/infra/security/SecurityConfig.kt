package com.evehunt.evehunt.global.infra.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val authenticationEntryPoint: AuthenticationEntryPoint
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .cors {  }
        .csrf { it.disable() }
        .authorizeHttpRequests{ requests ->
            requests.anyRequest().permitAll()
        }
        .sessionManagement{it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)}
        .addFilterBefore(jwtAuthenticationFilter, BasicAuthenticationFilter::class.java)
        .exceptionHandling{
            it.authenticationEntryPoint(authenticationEntryPoint)
        }
        .build()!!
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource
    {
        val configuration =  CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:3006")
        configuration.addAllowedHeader("*")
        configuration.addAllowedMethod("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}