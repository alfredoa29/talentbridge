package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CompanyBasicDto
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.domain.model.AreaOfInterest
import com.example.oportunia.domain.model.Company
import com.example.oportunia.domain.model.Internship
import javax.inject.Inject

class InternshipMapper @Inject constructor() {

    // Mapea Internship a InternshipDto
    fun mapToDto(internship: Internship): InternshipDto {
        return InternshipDto(
            id = internship.id,
            title = internship.title,
            shortDescription = internship.shortDescription,
            description = internship.description,
            active = internship.active,

            company = CompanyBasicDto(internship.company.id, internship.company.pictureUrl,internship.company.name), // Usamos CompanyBasicDto directamente
            category = AreaOfInterestDto(internship.category.id, internship.category.name)
        )
    }

    fun mapToDtoList(internships: List<Internship>): List<InternshipDto> {
        return internships.map { mapToDto(it) }
    }

    // Mapea InternshipDto a Internship
    fun mapToDomain(internshipDto: InternshipDto): Internship {
        return Internship(
            id = internshipDto.id,
            title = internshipDto.title,
            shortDescription = internshipDto.shortDescription,
            description = internshipDto.description,
            active = internshipDto.active,

            company = Company(
                id = internshipDto.company.id,
                name = internshipDto.company.name,
                email = "",  // Valor por defecto
                password = "",  // Valor por defecto
                enabled = true,  // Valor por defecto
                tokenExpired = false,  // Valor por defecto
                website = "",  // Valor por defecto
                pictureUrl = internshipDto.company.pictureUrl,

                internships = emptyList(), // Valor por defecto,
                sectorList = emptyList()  // Valor por defecto
            ),
            category = AreaOfInterest(
                id = internshipDto.category.id,
                name = internshipDto.category.name
            )
        )
    }

    // Mapea Internship a CreateInternshipDto
    fun mapToCreateInternshipDto(internship: Internship): InternshipInputDto {
        return InternshipInputDto(

            title = internship.title,
            shortDescription = internship.shortDescription,
            description = internship.description,
            active = internship.active,
            companyId = internship.company.id,
            categoryId = internship.category.id
        )
    }



    fun mapToDomainList(internshipsDto: List<InternshipDto>): List<Internship> {
        return internshipsDto.map { mapToDomain(it) }
    }
}
