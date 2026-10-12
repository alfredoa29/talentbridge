package com.example.oportunia.domain.model

import java.util.Date

open class User(
    open val id: Long,
    open val email: String,
    open val password: String,
    open val enabled: Boolean,
    open val tokenExpired: Boolean
)
