package edu.backend.oportuniapp

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.jdbc.Sql
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test


@Profile("initlocal")
@SpringBootTest
@Sql(scripts = ["/cleanup.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql("/import-database.sql")

class LoadInitData(
    @Autowired
    val studentRepository: StudentRepository,
    @Autowired
    val companyRepository: CompanyRepository,

    @Autowired
    val internshipRepository: InternshipRepository,

    @Autowired
    val skillRepository: SkillRepository,

    @Autowired
    val countryRepository: CountryRepository,

    @Autowired
    val genderRepository: GenderRepository,

    @Autowired
    val areaOfInterestRepository: AreaOfInterestRepository,

) {


    @Test
    fun testStudentFindAll(){
        val students = studentRepository.findAll()
        Assertions.assertTrue(students.size == 2)
    }




    @Test
    fun testStudentFindById() {
        val student: Student = studentRepository.findById(1).get()
        Assertions.assertTrue(student.id?.toInt() == 1)
    }



    @Test
    fun testCompanyFindById() {
        val company: Company = companyRepository.findById(4).get()
        Assertions.assertTrue(company.id?.toInt() == 4)
    }



   @Test
   fun testInternshipFindAll(){
       val internship = internshipRepository.findAll()
       Assertions.assertTrue(internship.size == 3)
   }




   @Test
   fun testInternshipFindById() {
       val internship: Internship = internshipRepository.findById(1).get()
       Assertions.assertTrue(internship.id?.toInt() == 1)
   }





   @Test
   fun testSkillFindById() {
       val skill: Skill = skillRepository.findById(1).get()
       Assertions.assertTrue(skill.id?.toInt() == 1)
   }




    @Test
    fun testSaveStudent() {
        // Crear objetos relacionados requeridos (o traerlos de BD si ya existen)
        val country = countryRepository.findById(1).get()
        val gender = genderRepository.findById(1).get()
        val skill = skillRepository.findById(1).get()
        val area = areaOfInterestRepository.findById(1).get()

        // Crear el Student
        val student = Student(

            username = "karen98",
            fullName = "Karen López",
            university = "UNA",
            profilePicUrl = "http://example.com/karen.jpg",
            country = country,
            gender = gender,
            career = "Ingenieria en Informatica",
            skillList = mutableListOf(skill),
            areaOfInterestList = mutableListOf(area),
        ).apply {

            email = "karen@example.com"
            password = "123456"
            enabled = true
            tokenExpired = false
        }


        val savedStudent = studentRepository.save(student)

        Assertions.assertNotNull(savedStudent.id)
        Assertions.assertEquals("Karen López", savedStudent.fullName)
    }





}