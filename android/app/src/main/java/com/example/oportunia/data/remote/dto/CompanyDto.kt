package com.example.oportunia.data.remote.dto

data class CompanyDto(
    val id: Long,
    val name: String,
    val website: String,
    val pictureUrl: String,
    val internships: List<InternshipDto>,
    val sectorList: List<SectorDto>
) {
}