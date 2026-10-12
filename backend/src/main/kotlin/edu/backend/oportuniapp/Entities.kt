package edu.backend.oportuniapp

import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users") //este nombre es porque si pongo "user" parece que la bd da error
open class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    var email: String = "",
    var password: String = "",

    @Temporal(TemporalType.TIMESTAMP)
    var createDate: Date = Date(),

    var enabled: Boolean? = null,
    var tokenExpired: Boolean? = null,


    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")]
    )
    var roleList: MutableSet<Role> = mutableSetOf(),

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (email != other.email) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "User(id=$id, email='$email', password='$password', createDate=$createDate, enabled=$enabled, tokenExpired=$tokenExpired, roleList=$roleList)"
    }
}

@Entity
@PrimaryKeyJoinColumn(name = "id") // explícitamente indica que hereda la PK de User
@Table(name = "student")
class Student(

    @Column(nullable = true)
    var username: String? = null,

    @Column(nullable = true)
    var fullName: String? = null,

    @Column(nullable = true)
    var university: String? = null,

    @Column(nullable = true)
    var career: String? = null,

    @Column(nullable = true)
    var profilePicUrl: String? = null,

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false, referencedColumnName = "id")
    var country: Country = Country(),

    @ManyToMany
    @JoinTable(
    name = "student_skill",
    joinColumns = [JoinColumn(name = "student_id", referencedColumnName = "id")],
    inverseJoinColumns = [JoinColumn(name = "skill_id", referencedColumnName = "id")]
    )
    var skillList: MutableList<Skill> = mutableListOf(),

    @ManyToMany
    @JoinTable(
        name = "student_areas_of_interest",
        joinColumns = [JoinColumn(name = "student_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "area_of_interest_id", referencedColumnName = "id")]
    )
    var areaOfInterestList: MutableList<AreaOfInterest> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "gender_id", nullable = true, referencedColumnName = "id")
    var gender: Gender = Gender(),

): User() {
    constructor() : this(null,null,null,null,null)



    override fun toString(): String {
        return "Student(username='$username', fullName='$fullName', university='$university', career='$career', profilePicUrl='$profilePicUrl', country=$country, gender=$gender)"
    }

}


@Entity
@PrimaryKeyJoinColumn(name = "id") // explícitamente indica que hereda la PK de User
@Table(name = "company")
class Company(
    var name: String,
    var website: String,
    var pictureUrl: String,
    @ManyToMany
    @JoinTable(
        name = "company_sector",
        joinColumns = [JoinColumn(name = "company_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "sector_id", referencedColumnName = "id")]
    )
    var sectorList: MutableSet<Sector> = mutableSetOf(),

    @OneToMany(mappedBy = "company")
    var internshipList: MutableSet<Internship> = mutableSetOf(),


    ): User() {

    constructor() : this("", "", "")

    override fun toString(): String {
        return "Company(name='$name', website='$website', sectorList=$sectorList)"
    }
}



@Entity
@Table(name = "sector")
data class Sector(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,

    @ManyToMany
    var companies: MutableList<Company> = mutableListOf()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sector

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Sector(id=$id, name='$name', companies=$companies)"
    }
}

@Entity
@Table(name = "area_of_interest")
data class AreaOfInterest(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String = "",

    //esto puede ir en student o aqui en esta clase
    @ManyToMany(mappedBy = "areaOfInterestList")

    var studentList: MutableList<Student> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AreaOfInterest

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "AreaOfInterest(id=$id, name='$name', studentList=$studentList)"
    }
}


@Entity
@Table(name = "country")
data class Country(
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   var id: Long? = null,
   var name: String = "",

   @OneToMany(mappedBy = "country")
   var studentList: MutableList<Student> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Country) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Country(id=$id, name=$name, studentList=$studentList)"
    }
}


@Entity
@Table(name = "skill")
data class Skill(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String,


    @ManyToMany(fetch = FetchType.LAZY)
    var studentList: MutableList<Student> = mutableListOf()

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Skill

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Skill(id=$id, name='$name', studentList=$studentList)"
    }
}


@Entity
@Table(name = "gender")
data class Gender(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var name: String= "",

    @OneToMany(mappedBy = "gender")
    var studentList: MutableList<Student> = mutableListOf()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Gender

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Gender(id=$id, name='$name', studentList=$studentList)"
    }
}


@Entity
@Table(name = "role")
data class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String,
    //Entity Relationship
    @ManyToMany
    @JoinTable(
        name = "role_privilege",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "privilege_id", referencedColumnName = "id")]
    )
    var privilegeList:  MutableList<Privilege> = mutableListOf()

) {
    constructor(): this(null, "")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Role) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Role(id=$id, name='$name', privilegeList=$privilegeList)"
    }
}


@Entity
@Table(name = "privilege")
data class Privilege(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,
    var name: String,

    // Entity Relationship


    @ManyToMany(fetch = FetchType.LAZY)
    var userList:  MutableSet<User> = mutableSetOf(),
    @ManyToMany(fetch = FetchType.LAZY)
    var roleList:  MutableSet<Role> = mutableSetOf(),



    ) {
    constructor(): this(null, "")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Privilege) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Privilege(id=$id, name='$name', userList=$userList, roleList=$roleList)"
    }
}

@Entity
@Table(name = "internship")
data class Internship(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var title: String,

    @Column(columnDefinition = "TEXT")
    var shortDescription: String,

    @Column(columnDefinition = "TEXT")
    var description: String,

    var active: Boolean,

    @ManyToOne
    var category: AreaOfInterest,

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true, referencedColumnName = "id")
    var company: Company? = null ,

    @OneToMany(mappedBy = "internship") //internship es l nombre de la var en Application
    var aplicationList: MutableList<Application> = mutableListOf()

) {
    constructor() : this(null,"", "", "", true, AreaOfInterest())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Internship) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Internship(id=$id, title='$title', description='$description', company=$company)"
    }
}

@Entity
@Table(name = "application")
data class Application(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "application_status_id", nullable = false, referencedColumnName = "id")
    var status: ApplicationStatus,

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false, referencedColumnName = "id")
    var student: Student,

    @ManyToOne
    @JoinColumn(name = "internship_id", nullable = false, referencedColumnName = "id")
    var internship: Internship
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Application) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "Application(id=$id, status='${status.name}', student=$student, internship=$internship)"
    }
}

@Entity
@Table(name = "application_status")
data class ApplicationStatus(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,

    @Column(name = "name", length = 255, nullable = false)
    var name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ApplicationStatus

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "ApplicationStatus(id=$id, name='$name')"
    }
}

@Entity
@Table(name = "user_role")
data class UserRole(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User,

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    var role: Role
)

data class InterviewAI(

    val introduction: QuestionSection,
    val knowledgeAssessment: QuestionSection,
    val problemSolving: ExerciseSection,
    val expectationsAndClosure: QuestionSection
)

data class QuestionSection(
    val questions: List<String>
)

data class ExerciseSection(
    val exercises: List<String>
)


data class AnalizeCurriculumAI(
    val strengths: List<String>,
    val weaknesses: List<String>,
    val improvementAreas: List<String>,
    val overallAssessment: String
)

data class StudentInternship(
    val applicationId: Long,
    val studentId: Long,
    val studentName: String,
    var profilePicUrl: String,
    val internshipId: Long,
    val internshipTitle: String,
    val companyName: String
)