package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.SkillDto
import com.example.oportunia.domain.model.Skill
import javax.inject.Inject



/**
 * Mapper class for converting between Skill domain entities and SkillDto data objects.
 */
class SkillMapper @Inject constructor() {

    /**
     * Maps a SkillDto to a domain Skill entity
     *
     * @param skillDto The data layer skill object to convert
     * @return Domain Skill object
     */
    fun mapToDomain(skillDto: SkillDto): Skill {
        return Skill(
            id = skillDto.id,
            name = skillDto.name
        )
    }

    /**
     * Maps a list of SkillDto objects to domain Skill entities
     *
     * @param skillsDto List of data layer skill objects to convert
     * @return List of domain Skill objects
     */
    fun mapToDomainList(skillsDto: List<SkillDto>): List<Skill> {
        return skillsDto?.map { mapToDomain(it) } ?: emptyList() // Si skillsDto es null, retorna una lista vací
    }

    /**
     * Maps a domain Skill to a SkillDto
     *
     * @param skill The domain layer skill object to convert
     * @return SkillDto object for data layer
     */
    fun mapToDto(skill: Skill): SkillDto {
        return SkillDto(
            id = skill.id,
            name = skill.name
        )
    }

    /**
     * Maps a list of domain Skill objects to SkillDto entities
     *
     * @param skills List of domain skill objects to convert
     * @return List of SkillDto objects
     */
    fun mapToDtoList(skills: List<Skill>): List<SkillDto> {
        return skills.map { mapToDto(it) }
    }
}
