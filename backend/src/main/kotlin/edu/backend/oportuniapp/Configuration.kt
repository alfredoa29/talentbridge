package edu.backend.oportuniapp

import edu.backend.oportuniapp.AppCustomDsl.Companion.customDsl
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Profile("initlocal")
@Configuration
@EnableWebSecurity
class OpensecurityConfiguration {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf {
            it.disable()
        }
        .cors {
            it.disable()
        }
        .authorizeHttpRequests {
            it.anyRequest().permitAll()
        }

        return http.build()
    }
}

@Profile("!initlocal")
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
class JwtSecurityConfiguration {

    @Value("\${url.unsecure}")
    val URL_UNSECURE: String? = null

    @Value("\${url.studentRegister}")
    val URL_SIGNUP_STUDENT: String? = null

    @Value("\${url.companyRegister}")
    val URL_SIGNUP_COMPANY: String? = null

    @Resource
    private val userDetailsService: AppUserDetailsService? = null

    @Bean
    @Throws(java.lang.Exception::class)
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager? {
        return authConfig.authenticationManager
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider? {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder())
        return authProvider
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf {
            it.disable()
        }
        .cors {
            it.configurationSource(corsConfigurationSource())
        }
        .authorizeHttpRequests {
            it
                .requestMatchers("/".plus(URL_UNSECURE).plus("/**")).permitAll()
                .requestMatchers(HttpMethod.POST, URL_SIGNUP_STUDENT).permitAll()
                .requestMatchers(HttpMethod.POST, URL_SIGNUP_COMPANY).permitAll()
                .requestMatchers("/**").authenticated()
        }
        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }
        .authenticationProvider(authenticationProvider())
        .apply(customDsl())

        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().apply {
            allowCredentials = true
            addAllowedOrigin("http://localhost:8080")
            addAllowedHeader("*")
            addAllowedMethod("*")
        }
        source.registerCorsConfiguration("/**", config)
        return source
    }
}

class AppCustomDsl : AbstractHttpConfigurer<AppCustomDsl?, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        val authenticationManager = http.getSharedObject(
            AuthenticationManager::class.java
        )

        http.addFilter(JwtAuthenticationFilter(authenticationManager))
        http.addFilter(JwtAuthorizationFilter(authenticationManager))
    }
    companion object {
        fun customDsl(): AppCustomDsl {
            return AppCustomDsl()
        }
    }

}