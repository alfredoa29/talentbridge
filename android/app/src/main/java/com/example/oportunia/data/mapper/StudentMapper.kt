package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.StudentDto
import com.example.oportunia.domain.model.Student
import javax.inject.Inject

/**
 * Mapper class for converting between Student domain entities and StudentDto data objects.
 * Delegates mapping of nested objects (Skills) to specialized mappers.
 */
class StudentMapper @Inject constructor(
    private val skillMapper: SkillMapper, // Mapeador para las habilidades (Skills)
    private val areaOfInterestMapper: AreaOfInterestMapper,
    private val countryMapper: CountryMapper,
    private val genderMapper: GenderMapper
) {

    /**
     * Maps a StudentDto to a domain Student entity.
     *
     * @param studentDto The data layer student object to convert.
     * @return Domain Student object.
     */
    fun mapToDomain(studentDto: StudentDto): Student {
        return Student(
            id = studentDto.id,
            email = studentDto.email,  // Se podría omitir o usar un valor por defecto
            password = "",  // Se podría omitir o usar un valor por defecto
            enabled = true,  // O asignar un valor por defecto
            //createDate = "",  // O asignar un valor por defecto
            tokenExpired = false,  // O asignar un valor por defecto
            fullName = studentDto.fullName,
            university = studentDto.university,
            career = studentDto.career,
            username = studentDto.username,
            skillList = skillMapper.mapToDomainList(studentDto.skillList ?: emptyList()), // Si skills es null, asigna una lista vacía
            areaOfInterestList = areaOfInterestMapper.mapToDomainList(studentDto.areaOfInterestList ?: emptyList()), // Si areaOfInterests es null, asigna una lista vacía
            profilePicUrl = studentDto.profilePicUrl,
            country = countryMapper.mapToDomain(studentDto.country),
            gender = genderMapper.mapToDomain(studentDto.gender )



            // No se mapea internships ya que no existe en el DTO
        )
    }

    /**
     * Maps a list of StudentDto objects to domain Student entities.
     *
     * @param studentsDto List of data layer student objects to convert.
     * @return List of domain Student objects.
     */
    fun mapToDomainList(studentsDto: List<StudentDto>): List<Student> {
        return studentsDto.map { mapToDomain(it) }
    }

    /**
     * Maps a domain Student to a StudentDto.
     *
     * @param student The domain layer student object to convert.
     * @return StudentDto object for data layer.
     */
    fun mapToDto(student: Student): StudentDto {
        return StudentDto(
            id = student.id,
            fullName = student.fullName,
            university = student.university,
            career = student.career,
            username = student.username,
            email = student.email,
            skillList = skillMapper.mapToDtoList(student.skillList), // Convierte la lista de Skills a solo nombres
            areaOfInterestList = areaOfInterestMapper.mapToDtoList(student.areaOfInterestList),
            profilePicUrl = student.profilePicUrl,
            country = countryMapper.mapToDto(student.country),
            gender = genderMapper.mapToDto(student.gender)

            // No se mapea internships ya que no existe en el DTO
        )
    }

    /**
     * Maps a list of domain Student objects to StudentDto entities.
     *
     * @param students List of domain student objects to convert.
     * @return List of StudentDto objects.
     */
    fun mapToDtoList(students: List<Student>): List<StudentDto> {
        return students.map { mapToDto(it) }
    }
}
