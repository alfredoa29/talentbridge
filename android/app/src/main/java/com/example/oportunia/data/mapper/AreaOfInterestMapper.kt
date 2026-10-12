package com.example.oportunia.data.mapper


import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.domain.model.AreaOfInterest
import javax.inject.Inject

/**
 * Mapper class for converting between AreaOfInterest domain entities and AreaOfInterestDto data objects.
 */
class AreaOfInterestMapper @Inject constructor() {

    /**
     * Maps an AreaOfInterestDto to a domain AreaOfInterest entity.
     *
     * @param areaOfInterestDto The data layer area of interest object to convert
     * @return Domain AreaOfInterest object
     */
    fun mapToDomain(areaOfInterestDto: AreaOfInterestDto): AreaOfInterest {
        return AreaOfInterest(
            id = areaOfInterestDto.id,
            name = areaOfInterestDto.name
        )
    }

    /**
     * Maps a list of AreaOfInterestDto objects to domain AreaOfInterest entities.
     *
     * @param areasOfInterestDto List of data layer area of interest objects to convert
     * @return List of domain AreaOfInterest objects
     */
    fun mapToDomainList(areasOfInterestDto: List<AreaOfInterestDto>?): List<AreaOfInterest> {
        return areasOfInterestDto?.map { mapToDomain(it) } ?: emptyList() // Si es null, retornamos una lista vacía
    }

    /**
     * Maps a domain AreaOfInterest to an AreaOfInterestDto.
     *
     * @param areaOfInterest The domain layer area of interest object to convert
     * @return AreaOfInterestDto object for data layer
     */
    fun mapToDto(areaOfInterest: AreaOfInterest): AreaOfInterestDto {
        return AreaOfInterestDto(
            id = areaOfInterest.id,
            name = areaOfInterest.name
        )
    }

    /**
     * Maps a list of domain AreaOfInterest objects to AreaOfInterestDto entities.
     *
     * @param areasOfInterest List of domain area of interest objects to convert
     * @return List of AreaOfInterestDto objects
     */
    fun mapToDtoList(areasOfInterest: List<AreaOfInterest>): List<AreaOfInterestDto> {
        return areasOfInterest.map { mapToDto(it) }
    }
}
