package edu.backend.oportuniapp

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>
}

@Repository
interface UserRoleRepository : JpaRepository<UserRole, Long> { }

@Repository
interface StudentRepository : JpaRepository<Student, Long> {
    fun findByEmail(email: String): Optional<Student>
}

@Repository
interface CompanyRepository : JpaRepository<Company, Long> {
    fun findByName(name: String): Optional<Company>
}

@Repository
interface SectorRepository : JpaRepository<Sector, Long> {
    fun findByName(name: String): Optional<Sector>
}

@Repository
interface AreaOfInterestRepository : JpaRepository<AreaOfInterest, Long> {
    fun findByName(name: String): Optional<AreaOfInterest>
}

@Repository
interface CountryRepository : JpaRepository<Country, Long> {
    fun findByName(name: String): Optional<Country>
}

@Repository
interface SkillRepository : JpaRepository<Skill, Long> {
    fun findByName(name: String): Optional<Skill>
}

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: String): Optional<Role>
}

@Repository
interface PrivilegeRepository : JpaRepository<Privilege, Long> {
    fun findByName(name: String): Optional<Privilege>
}

@Repository
interface InternshipRepository : JpaRepository<Internship, Long> {
    @Query("""
    SELECT i FROM Internship i
    WHERE i.category IN :interests
    AND i.active = true
    AND i.id NOT IN (
        SELECT a.internship.id FROM Application a WHERE a.student.id = :studentId
    )
""")
    fun findRelevantInternshipsForStudent(
        @Param("interests") interests: List<AreaOfInterest>,
        @Param("studentId") studentId: Long
    ): List<Internship>

    fun findAllByCompanyId(companyId: Long): List<Internship>
}

@Repository
interface ApplicationRepository : JpaRepository<Application, Long> {
    fun findByStudentIdAndInternshipId(studentId: Long, internshipId: Long): Optional<Application>
    fun findAllByStudentId(studentId: Long): List<Application>
    @Query("""
        SELECT DISTINCT a.student 
        FROM Application a 
        WHERE a.internship.company.id = :companyId
    """)
    fun findStudentsByCompanyId(@Param("companyId") companyId: Long): List<Student>

    @Query("""
        SELECT new edu.backend.oportuniapp.StudentInternship(
            a.id,
            a.student.id, 
            a.student.fullName, 
            a.student.profilePicUrl,
            a.internship.id, 
            a.internship.title, 
            a.internship.company.name
        )
        FROM Application a 
        WHERE a.internship.company.id = :companyId
    """)
    fun findStudentsAndInternshipsByCompanyId(@Param("companyId") companyId: Long): List<StudentInternship>
}

@Repository
interface ApplicationStatusRepository : JpaRepository<ApplicationStatus, Long> { }

@Repository
interface GenderRepository : JpaRepository<Gender, Long> {}

