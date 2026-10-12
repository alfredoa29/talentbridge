package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.SectorDto
import com.example.oportunia.data.remote.dto.SkillDto
import com.example.oportunia.domain.model.Sector
import com.example.oportunia.domain.model.Skill
import javax.inject.Inject

class SectorMapper @Inject constructor() {
    fun mapToDomain(sectorDto: SectorDto): Sector {
        return Sector(
            id = sectorDto.id,
            name = sectorDto.name
        )
    }

    fun mapToDomainList(sectorsDto: List<SectorDto>): List<Sector> {
        return sectorsDto?.map { mapToDomain(it) } ?: emptyList()
    }

    fun mapToDto(sector: Sector): SectorDto {
        return SectorDto(
            id = sector.id,
            name = sector.name
        )
    }

    fun mapToDtoList(sectors: List<Sector>): List<SectorDto> {
        return sectors.map { mapToDto(it) }
    }
}