package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.CompanyBasicDto
import com.example.oportunia.data.remote.dto.CompanyDto

import com.example.oportunia.domain.model.Company
import javax.inject.Inject

class CompanyMapper @Inject constructor(
    private val sectorMapper: SectorMapper,
) {

    // Mapea Company a CompanyDto
    fun mapToDto(company: Company): CompanyDto {
        return CompanyDto(
            id = company.id,
            name = company.name,
            website = company.website,
            pictureUrl = company.pictureUrl,
            internships = company.internships.map { InternshipMapper().mapToDto(it) }, // Ahora, InternshipMapper es usado directamente solo en esta parte.
            sectorList = sectorMapper.mapToDtoList(company.sectorList)
            )
    }

    // Mapea CompanyDto a Company
    fun mapToDomain(dto: CompanyDto): Company {
        return Company(
            id = dto.id,
            email = "",
            password = "",
            enabled = true,
            tokenExpired = false,
            name = dto.name,
            website = dto.website ?: "",
            pictureUrl = dto.pictureUrl?:"",
            internships = emptyList(), // O puedes mapear internships si es necesario
            sectorList = sectorMapper.mapToDomainList(dto.sectorList ?: emptyList())
        )
    }

    // Mapea Company a CompanyBasicDto
    fun mapToBasicDto(company: Company): CompanyBasicDto {
        return CompanyBasicDto(
            id = company.id,
            name = company.name,
            pictureUrl = company.pictureUrl
        )
    }
}
