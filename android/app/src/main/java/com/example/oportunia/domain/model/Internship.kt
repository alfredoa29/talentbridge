package com.example.oportunia.domain.model

import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CompanyBasicDto

data class Internship(
    val id: Long,
    val title: String,
    val shortDescription: String,
    val description: String,
    val active: Boolean,
    val company: Company,
    val category: AreaOfInterest
  //  val applicants: List<Student> = emptyList()

)
