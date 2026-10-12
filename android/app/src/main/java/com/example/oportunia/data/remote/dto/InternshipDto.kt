package com.example.oportunia.data.remote.dto

data class InternshipDto(
    val id: Long,
    val title: String,
    val shortDescription: String,
    val description: String,
    val active: Boolean,
    val company: CompanyBasicDto,
    val category: AreaOfInterestDto
)