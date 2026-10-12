package edu.backend.oportuniapp

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.http.HttpHeaders
import org.apache.http.auth.AuthenticationException
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import java.io.IOException
import java.security.Key
import java.util.Date

object SecurityConstants {
    const val TOKEN_TYPE = "JWT"
    const val TOKEN_ISSUER = "oportunia-secure-api"
    const val TOKEN_AUDIENCE = "oportunia-secure-app"
    const val TOKEN_LIFETIME = 864000000
    const val TOKEN_PREFIX = "Bearer "
    const val APPLICATION_JSON = "application/json"
    const val UTF_8 = "UTF-8"
    // Read from the environment; the fallback is only for local development.
    val TOKEN_SECRET: String =
        System.getenv("JWT_SECRET")
            ?: "local-development-only-secret-change-me-0123456789-0123456789-0123456789"
}

class JwtAuthenticationFilter(authenticationManager: AuthenticationManager) : UsernamePasswordAuthenticationFilter() {

    private val authManager: AuthenticationManager

    init {
        setFilterProcessesUrl("/v1/users/login")
        authManager = authenticationManager
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): Authentication {

        if (request?.method != "POST") {
            throw AuthenticationServiceException("Authentication method ${request?.method} not supported.")
        }

        return try {
            val userInput: UserInput = ObjectMapper()
                .readValue(request.inputStream, UserInput::class.java)

            if (userInput.email.isBlank() || userInput.password.isBlank()) {
                throw AuthenticationServiceException("Email and password must not be empty.")
            }

            authManager.authenticate(
                UsernamePasswordAuthenticationToken(
                    userInput.email,
                    userInput.password,
                    ArrayList()
                )
            )
        }
        catch (exception: IOException) {
            throw RuntimeException(exception)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        filterChain: FilterChain?,
        authentication: Authentication?
    ) {
        val objectMapper = ObjectMapper()

        val token = Jwts.builder()
            .signWith(key(), SignatureAlgorithm.HS512)
            .setHeaderParam("type", SecurityConstants.TOKEN_TYPE)
            .setIssuer(SecurityConstants.TOKEN_ISSUER)
            .setAudience(SecurityConstants.TOKEN_AUDIENCE)
            .setSubject((authentication?.principal as org.springframework.security.core.userdetails.User).username)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.TOKEN_LIFETIME))
            .compact()

        response?.addHeader(HttpHeaders.AUTHORIZATION, SecurityConstants.TOKEN_PREFIX + token)


        val out = response?.writer

        response?.contentType = SecurityConstants.APPLICATION_JSON
        response?.characterEncoding = SecurityConstants.UTF_8

        out?.print(objectMapper.writeValueAsString(authentication.principal))
        out?.flush()
    }
}

private fun key(): Key {
    return Keys.hmacShaKeyFor(SecurityConstants.TOKEN_SECRET.toByteArray(Charsets.UTF_8))
}

class JwtAuthorizationFilter(authenticationManager: AuthenticationManager) :
        BasicAuthenticationFilter(authenticationManager) {

    @Throws(IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authorizationToken = request.getHeader(HttpHeaders.AUTHORIZATION)

        if((authorizationToken != null) && (authorizationToken.startsWith(SecurityConstants.TOKEN_PREFIX))) {
            authorizationToken = authorizationToken.replaceFirst(SecurityConstants.TOKEN_PREFIX.toRegex(), "")

            val claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(authorizationToken)
                .body

            val username = claims.subject

            LoggedUser.logIn(username)

            SecurityContextHolder.getContext().authentication =
                UsernamePasswordAuthenticationToken(username, null, emptyList())
        }
        filterChain.doFilter(request, response)
    }
        }


object LoggedUser {
    private val userHolder = ThreadLocal<String>()

    fun logIn(user: String){
        userHolder.set(user)
    }

    fun logOut() {
        userHolder.remove()
    }

    fun get(): String {
        return userHolder.get()
    }
}