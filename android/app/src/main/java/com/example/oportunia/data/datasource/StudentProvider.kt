package com.example.oportunia.data.datasource



import com.example.oportunia.domain.model.AreaOfInterest
import com.example.oportunia.domain.model.Country
import com.example.oportunia.domain.model.Gender
import com.example.oportunia.domain.model.Skill
import com.example.oportunia.domain.model.Student

object StudentProvider {
    // Lista inmutable de estudiantes con todos los campos de User + Student
    private val studentList = listOf(
        Student(
            id = 1L,
            email = "maria.garcia@example.com",
            password = "password123",
            enabled = true,

            tokenExpired = false,
            fullName = "María García",
            username = "algo",
            university = "Universidad Nacional",
            career = "Ingeniería de Software",
            skillList = listOf(Skill(1L, "Kotlin"), Skill(2L, "Android"), Skill(3L, "Firebase")),
            areaOfInterestList = listOf(
                AreaOfInterest(id = 1L, name = "Desarrollo de Software"),
                AreaOfInterest(id = 2L, name = "Inteligencia Artificial"),
                AreaOfInterest(id = 3L, name = "Ciberseguridad"),

                ),
            profilePicUrl = "",
            country = Country(1,"Costa Rica"),
            gender = Gender(1,"M")

        ),
        Student(
            id = 2L,
            email = "carlos.mendez@example.com",
            password = "password123",
            enabled = true,

            tokenExpired = false,
            fullName = "Carlos Méndez",
            username = "algo",
            university = "Universidad Tecnológica",
            career = "Marketing Digital",
            skillList = listOf(Skill(4L, "SEO"), Skill(5L, "Redes Sociales"), Skill(6L, "Analytics")),
            areaOfInterestList = listOf(
                AreaOfInterest(id = 1L, name = "Desarrollo de Software"),
                AreaOfInterest(id = 2L, name = "Inteligencia Artificial"),
                AreaOfInterest(id = 3L, name = "Ciberseguridad"),

                ),
            profilePicUrl = "",
            country = Country(1,"Costa Rica"),
            gender = Gender(1,"M")

        ),
        Student(
            id = 3L,
            email = "luisa.fernandez@example.com",
            password = "password123",
            enabled = true,

            tokenExpired = false,
            fullName = "Luisa Fernández",
            username = "algo",
            university = "Instituto de Diseño",
            career = "Diseño Gráfico",
            skillList = listOf(Skill(7L, "Photoshop"), Skill(8L, "Illustrator"), Skill(9L, "Figma")),
            areaOfInterestList = listOf(
                AreaOfInterest(id = 1L, name = "Desarrollo de Software"),
                AreaOfInterest(id = 2L, name = "Inteligencia Artificial"),
                AreaOfInterest(id = 3L, name = "Ciberseguridad"),

                ),
            profilePicUrl = "",
            country = Country(1,"Costa Rica"),
            gender = Gender(1,"M")

        ),
    )

    // Mapa para búsquedas eficientes
    private val studentMap = studentList.associateBy { it.id }

    /**
     * Encuentra un estudiante por su ID
     */
    fun findStudentById(id: Long): Student? = studentMap[id]

    /**
     * Obtiene todos los estudiantes
     */
    fun findAllStudents(): List<Student> = studentList

    /**
     * Busca estudiantes por cualquier campo
     */
    fun searchStudents(query: String): List<Student> {
        val lowerQuery = query.lowercase()
        return studentList.filter {
            it.fullName.lowercase().contains(lowerQuery) ||
                    it.email.lowercase().contains(lowerQuery) ||
                    it.university.lowercase().contains(lowerQuery) ||
                    it.career.lowercase().contains(lowerQuery) ||
                    it.skillList.any { skill -> skill.name.lowercase().contains(lowerQuery) }
        }
    }

    /**
     * Autentica un estudiante por email y password
     */
    fun authenticate(email: String, password: String): Student? {
        return studentList.firstOrNull {
            it.email.equals(email, ignoreCase = true) && it.password == password
        }
    }
}
