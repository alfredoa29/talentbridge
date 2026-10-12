package com.example.oportunia.data.remote.dto

// DTO para el Estudiante (StudentDTO)
data class StudentDto(
    val id: Long,
    val fullName: String,
    val university: String,
    val career: String,
    val username: String,
    val email: String,
    val skillList: List<SkillDto>,  // Solo los nombres de las habilidades (en lugar de objetos completos de Skill)
    val areaOfInterestList: List<AreaOfInterestDto>,
    val profilePicUrl: String,
    val country: CountryDto,
    val gender: GenderDto
   // val internships: List<InternshipDto>  // Lista de DTOs de las pasantías
)
