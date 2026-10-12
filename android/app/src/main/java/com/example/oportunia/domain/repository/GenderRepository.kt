package com.example.oportunia.domain.repository

import com.example.oportunia.domain.model.Gender

interface GenderRepository{
    suspend fun getAllGenders(): Result<List<Gender>>
}