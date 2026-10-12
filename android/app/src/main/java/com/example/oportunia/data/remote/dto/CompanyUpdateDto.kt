package com.example.oportunia.data.remote.dto

data class CompanyUpdateDto (
    val id: Long,
    val name: String,
    val website: String,
    val sectorListIds: List<Long>
    )