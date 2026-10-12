package com.example.oportunia.data.remote.dto

data class InternshipInputDto(

    val id: Long? = null,
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var companyId:  Long? = null,
    var active: Boolean? = null,
    var categoryId: Long? = null,
)
