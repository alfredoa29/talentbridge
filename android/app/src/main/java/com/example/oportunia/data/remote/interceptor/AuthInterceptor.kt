package com.example.oportunia.data.remote.interceptor

import com.example.oportunia.data.local.AuthPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val authPreferences: AuthPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        if (originalRequest.url.encodedPath.contains("users/login") ||
            originalRequest.url.encodedPath.contains("students/register") ||
            originalRequest.url.encodedPath.contains("companies/register")) {
            return chain.proceed(originalRequest)
        }

        val token = runBlocking {
            authPreferences.getAuthToken()
        }

        if (token.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }

        val authenticatedRequest = originalRequest.newBuilder()
            .header("Authorization", "$token")
            .build()

        return chain.proceed(authenticatedRequest)
    }

}