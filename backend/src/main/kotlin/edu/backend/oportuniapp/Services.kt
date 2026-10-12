package edu.backend.oportuniapp


import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.transaction.Transactional
import org.springframework.ai.anthropic.AnthropicChatModel
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.messages.SystemMessage
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.model.ChatModel
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.openai.OpenAiChatModel
import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import org.springframework.core.io.Resource
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*
import kotlin.NoSuchElementException
import org.springframework.web.bind.annotation.*
//@Configuration -- solo para init-local profile?
class Encoder {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()
}

interface UserRoleService {
    fun assignRoleToUser(userId: Long, roleName: String): UserRole?
}
@Service
class AbstractUserRoleService (
    @Autowired
    val userRoleRepository: UserRoleRepository,
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val roleRepository: RoleRepository

) : UserRoleService {
    override fun assignRoleToUser(userId: Long, roleName: String): UserRole? {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found with ID $userId") }

        val role = roleRepository.findByName(roleName).get()

        val userRole = UserRole(user = user, role = role)

        return userRoleRepository.save(userRole)
    }
}

interface UserService {
    fun findAll(): List<UserResult>?
    fun findById(id: Long): UserResult?
    fun create(userInput: UserInput): UserResult?
    fun findByEmail(username: String): UserResult?
    fun update(userInput: UserInput): UserResult?
    fun deleteById(id: Long): UserResult?
}
@Service
class AbstractUserService(
    @Autowired
    val userRepository: UserRepository,
    val userMapper: UserMapper,
    val passwordEncoder: PasswordEncoder
) : UserService {
    override fun findAll(): List<UserResult>? {
        return userMapper.userToUserResultList(
            userRepository.findAll()
        )
    }

    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): UserResult? {
        val user: Optional<User> = userRepository.findById(id)
        if(user.isEmpty) {
            throw NoSuchElementException(String.format("The user with the id: %s does not exist.", id))
        }
        return userMapper.userToUserResult(
            user.get()
        )
    }

    @Throws(IllegalStateException::class)
    override fun create(userInput: UserInput): UserResult? {
        val user: User = userMapper.userInputToUser(userInput)

        if (user.password.isNotBlank()){
            user.password = passwordEncoder.encode(userInput.password)
        }

        return userMapper.userToUserResult(
            userRepository.save(user)
        )
    }

    override fun findByEmail(username: String): UserResult? {

        val user: Optional<User> = userRepository.findByEmail(username)
        if(user.isEmpty) {
            throw NoSuchElementException(String.format("The user with the username: %s does not exist.", username))
        }
        return userMapper.userToUserResult(
            user.get()
        )
    }

    @Throws(NoSuchElementException::class)
    override fun update(userInput: UserInput): UserResult? {
        val user: User = userRepository.findById(userInput.id!!)
            .orElseThrow { NoSuchElementException(String.format("The user with the id: %s does not exist.", userInput.id)) }

        if(userInput.email != user.email) {
            user.email = userInput.email
        }
        user.password = userInput.password

        return userMapper.userToUserResult(
            userRepository.save(user)
        )
    }

    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long): UserResult? {
        val user: User = userRepository.findById(id)
            .orElseThrow { NoSuchElementException(String.format("The user with the id: %s does not exist.", id)) }

        userRepository.delete(user)

        return userMapper.userToUserResult(
            user
        )
    }
}

// ----------------------------------------------------------------------------------------
interface AreaOfInterestService {
    fun findAll(): List<AreaOfInterestDetails>
}
@Service
class AbstractAreaOfInterestService(
      @Autowired
      val areaOfInterestRepository: AreaOfInterestRepository,
      @Autowired
      val areaOfInterestMapper: AreaOfInterestMapper

): AreaOfInterestService{
    override fun findAll(): List<AreaOfInterestDetails> {
        return areaOfInterestMapper.areaOfInterestToAreaOfInterestDetailsListDetails(
            areaOfInterestRepository.findAll()
        )
    }
}

interface StudentService {
    /**
     * Find all Students
     * @return a list of Students
     */
    fun findAll(): List<StudentResult>?

    /**
     * Get one Student by id
     * @param id of the Student
     * @return the Student found
     */
    fun findById(id: Long): StudentResult?

    /**
     * Save and flush a Student entity in the database
     * @param studentInput the dto input for Student
     * @return the Student created
     */
    fun create(studentInput: StudentInput): StudentResult?

    /**
     * Update a Student entity in the database
     * @param studentInput the dto input for Student
     * @return the updated Student
     */
    fun update(studentInput: StudentInput, image: MultipartFile?): StudentResult?

    /**
     * Delete a Student by id from Database
     * @param id of the Student
     */
    fun deleteById(id: Long)
}
@Service
class AbstractStudentService(
    @Autowired
    val studentRepository: StudentRepository,
    @Autowired
    val countryRepository: CountryRepository,
    @Autowired
    val genderRepository: GenderRepository,
    @Autowired
    val skillRepository: SkillRepository,
    @Autowired
    val areaOfInterestRepository: AreaOfInterestRepository,
    @Autowired
    val studentMapper: StudentMapper,
    @Autowired
    val cloudinaryService:CloudinaryService,
    val passwordEncoder: PasswordEncoder
) : StudentService {
    /**
     * Find all Students
     * @return a list of Students
     */
    override fun findAll(): List<StudentResult>? {
        return studentMapper.studentToStudentListResult(
            studentRepository.findAll()
        )
    }

    /**
     * Get one Student by id
     * @param id of the Student
     * @return the Student found
     */
    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): StudentResult? {
        val student: Optional<Student> = studentRepository.findById(id)
        if (student.isEmpty) {
            throw NoSuchElementException(String.format("The Student with the id: %s not found!", id))
        }
        return studentMapper.studentToStudentResult(
            student.get()
        )
    }

    /**
     * Save and flush a Student entity in the database
     * @param studentInput the dto input for Student
     * @return the Student created
     */
    @Throws(NoSuchElementException::class)
    override fun create(studentInput: StudentInput): StudentResult? {
        val student: Student = studentMapper.studentInputToStudent(studentInput)

        if(student.password.isNotBlank()){
           student.password = passwordEncoder.encode(student.password)
        }

        student.enabled = true
        student.tokenExpired = false

        // Obtener el país a partir de countryId
        val country = countryRepository.findById(studentInput.countryId!!)
            .orElseGet { null }

        // Asignar el país al estudiante
        student.country = country

        val gender = genderRepository.findById(studentInput.genderId!!).orElseGet { null }
        student.gender = gender
        // Obtener las habilidades a partir de skillIds
        val skillList: MutableList<Skill> = mutableListOf()
        for (skillId in studentInput.skillIds ?: emptyList()) {
            val skill = skillRepository.findById(skillId)
                .orElseGet { null }
            skillList.add(skill)
        }
        student.skillList = skillList

        // Obtener las áreas de interés a partir de areaOfInterestIds
        val areaOfInterestList: MutableList<AreaOfInterest> = mutableListOf()
        for (areaId in studentInput.areaOfInterestIds ?: emptyList()) {
            val area = areaOfInterestRepository.findById(areaId)
                .orElseGet { null }
            areaOfInterestList.add(area)
        }
        student.areaOfInterestList = areaOfInterestList

        val url = if (!studentInput.profilePicUrl.isNullOrBlank()) {
            studentInput.profilePicUrl
        } else {
            "https://res.cloudinary.com/dswfamqsi/image/upload/v1747552944/pfvtzaw9noquuvgd6gww.png"
        }
            student.profilePicUrl = url!!

        // Guardar el estudiante y mapearlo al resultado
        return studentMapper.studentToStudentResult(
            studentRepository.save(student)
        )
    }

    /**
     * Update a Student entity in the database
     * @param studentInput the dto input for Student
     * @return the updated Student
     */
    override fun update(studentInput: StudentInput, image: MultipartFile?): StudentResult? {
        val student = studentRepository.findById(studentInput.id!!)
            .orElseThrow { IllegalArgumentException("Student with id ${studentInput.id} not found") }

        // Actualizar campos simples
        student.fullName = studentInput.fullName ?: student.fullName
        student.username = studentInput.username ?: student.username
        student.university = studentInput.university ?: student.university
        student.career = studentInput.career ?: student.career
        student.profilePicUrl = studentInput.profilePicUrl ?: student.profilePicUrl

        // Actualizar country si viene el countryId
        studentInput.countryId?.let { countryId ->
            val country = countryRepository.findById(countryId)
                .orElseThrow { IllegalArgumentException("Country with id $countryId not found") }
            student.country = country
        }
        // Actualizar gender si viene el countryId
        studentInput.genderId?.let { genderId ->
            val gender = genderRepository.findById(genderId)
                .orElseThrow { IllegalArgumentException("GenderId with id $genderId not found") }
            student.gender = gender
        }

        // Actualizar skills si vienen skillIds
        val updatedSkillList = mutableListOf<Skill>()
        for (skillId in studentInput.skillIds ?: emptyList()) {
            val skill = skillRepository.findById(skillId)
                .orElseThrow { IllegalArgumentException("Skill with id $skillId not found") }
            updatedSkillList.add(skill)
        }
        student.skillList = updatedSkillList

        // Actualizar áreas de interés si vienen areaOfInterestIds
        val updatedAreaList = mutableListOf<AreaOfInterest>()
        for (areaId in studentInput.areaOfInterestIds ?: emptyList()) {
            val area = areaOfInterestRepository.findById(areaId)
                .orElseThrow { IllegalArgumentException("Area of Interest with id $areaId not found") }
            updatedAreaList.add(area)
        }

        student.areaOfInterestList = updatedAreaList

        // esto es para actualizar la foto de perfil
        // recibe una foto, y se utiliza cloudinaryService para subirla a la nube y retornal un link https publico
        // que se guarda en la db
        if (image != null && !image.isEmpty) {
            val uploadResult = cloudinaryService.uploadFile(image)
            student.profilePicUrl = uploadResult["secure_url"].toString()
        } else {
            student.profilePicUrl = studentInput.profilePicUrl ?: student.profilePicUrl
        }

        return studentMapper.studentToStudentResult(
            studentRepository.save(student)
        )
    }

    /**
     * Delete a Student by id from Database
     * @param id of the Student
     */
    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long) {
        if (!studentRepository.findById(id).isEmpty) {
            studentRepository.deleteById(id)
        } else {
            throw NoSuchElementException(String.format("The Student with the id: %s not found!", id))
        }
    }
}

// ----------------------------------------------------------------------------------------

interface CompanyService {
    fun findAll(): List<CompanyResult>?
    fun findById(id: Long): CompanyResult?
    fun create(companyInput: CompanyInput): CompanySimpleResult?
    fun update(companyInput: CompanyInput, image: MultipartFile?): CompanyResult?
    fun deleteById(companyId: Long)
}

@Service
class AbstractCompanyService(
    @Autowired
    val companyRepository: CompanyRepository,
    @Autowired
    val sectorRepository: SectorRepository,
    @Autowired
    val cloudinaryService: CloudinaryService,
    @Autowired
    val companyMapper: CompanyMapper,
    val passwordEncoder: PasswordEncoder
) : CompanyService {
    override fun findAll(): List<CompanyResult>? {
        return companyMapper.companyToCompanyListResult(
            companyRepository.findAll()
        )
    }

    override fun findById(id: Long): CompanyResult? {
       val company: Optional<Company> = companyRepository.findById(id)
       if(company.isEmpty) {
           throw java.util.NoSuchElementException(String.format("The Company with the id: %s not found!", id))
       }
       return companyMapper.companyToCompanyResult(
           company.get()
       )
    }

    override fun create(companyInput: CompanyInput): CompanySimpleResult? {
        val company: Company = companyMapper.companyInputToCompany(companyInput)

        if(company.password.isNotBlank()) {
            company.password = passwordEncoder.encode(company.password)
        }

        company.enabled = true
        company.tokenExpired = false

        // Obtener los sectores a partir de sectorListIds
        val sectorList: MutableSet<Sector> = mutableSetOf()
        for (sectorId in companyInput.sectorListIds?: emptyList()) {
            val sector =  sectorRepository.findById(sectorId).orElseGet { null }
            sectorList.add(sector)
        }
        company.sectorList = sectorList

        val url = if (!companyInput.pictureUrl.isNullOrBlank()) {
            companyInput.pictureUrl
        } else {
            "https://res.cloudinary.com/dswfamqsi/image/upload/v1747552944/pfvtzaw9noquuvgd6gww.png"
        }
        company.pictureUrl = url!!

        return companyMapper.companyToCompanySimpleResult(
            companyRepository.save(company)
        )
    }

    @Throws(java.util.NoSuchElementException::class)
    override fun update(companyInput: CompanyInput, image: MultipartFile?): CompanyResult? {
        val company = companyRepository.findById(companyInput.id!!)
            .orElseThrow { IllegalArgumentException("Company with id ${companyInput.id} not found") }

        // Actualizar campos simples
        company.name = companyInput.name ?: company.name
        company.website = companyInput.website ?: company.website


        // Actualizar sectores si vienen skillIds

        val updatedSectorList = mutableSetOf<Sector>()
        for (sectorId in companyInput.sectorListIds ?: emptyList()) {
            val sector = sectorRepository.findById(sectorId)
                .orElseThrow { IllegalArgumentException("Sector with id $sectorId not found") }
            updatedSectorList.add(sector)
        }
        company.sectorList = updatedSectorList



        // Manejo de la imagen del logotipo
        if (image != null && !image.isEmpty) {
            val uploadResult = cloudinaryService.uploadFile(image)
            company.pictureUrl = uploadResult["secure_url"].toString()
        } else {
            company.pictureUrl = companyInput.pictureUrl ?: company.pictureUrl
        }

        return companyMapper.companyToCompanyResult(
            companyRepository.save(company)
        )
    }
    
    /*override fun update(companyInput: CompanyInput): CompanyResult? {
        val company: Optional<Company> = companyRepository.findById(companyInput.id!!)
        if (company.isPresent) {
            throw java.util.NoSuchElementException(String.format("The Company with the id: %s not found!", companyInput.id))
        }
        val companyUpdated: Company = company.get()
        companyMapper.companyInputToCompany(companyInput, companyUpdated)
        return  companyMapper.companyToCompanyResult(companyRepository.save(companyUpdated))
    }*/

    @Throws(NoSuchElementException::class)
    override fun deleteById(companyId: Long) {
        if (!companyRepository.findById(companyId).isEmpty) {
            companyRepository.deleteById(companyId)
        } else {
            throw java.util.NoSuchElementException(String.format("The Company with the id: %s not found!", companyId))
        }

    }
}

// ----------------------------------------------------------------------------------------

interface InternshipService {
    fun findAll(): List<InternshipResult>?
    fun findById(id: Long): InternshipResult?
    fun create(internshipInput: InternshipInput): InternshipResult?
    fun update(internshipInput: InternshipInput): InternshipResult?
    fun  getFilteredInternships(studentId: Long): List<InternshipResult>?
    fun findAllByCompanyId(companyId: Long): List<InternshipResult>?
    fun deleteById(internshipId: Long)
}
@Service
class AbstractInternshipService(
    @Autowired
    val internshipRepository: InternshipRepository,
    @Autowired
    val internshipMapper: InternshipMapper,
    @Autowired
    private val areaOfInterestRepository: AreaOfInterestRepository,

    @Autowired
    val studentRepository: StudentRepository,

    private val companyRepository: CompanyRepository,

    ofInterestRepository: AreaOfInterestRepository

) : InternshipService {
    override fun findAll(): List<InternshipResult>? {
        return internshipMapper.internshipToInternshipResult(
            internshipRepository.findAll()
        )
    }

    override fun findById(id: Long): InternshipResult? {
        val internship: Optional<Internship> = internshipRepository.findById(id)
        if(internship.isEmpty) {
            throw java.util.NoSuchElementException(String.format("The Internship with the id: %s not found!", id))
        }
        return internshipMapper.internshipToInternshipResult(
            internship.get()
        )
    }

    override fun create(internshipInput: InternshipInput): InternshipResult? {
        val internship: Internship = internshipMapper.internshipInputToInternship(internshipInput)

        // Obtener el company  a partir de companmyid
        val company: Optional<Company> =  companyRepository.findById(internshipInput.companyId!!)
        if (company.isEmpty) {
            throw java.util.NoSuchElementException(String.format("The Company with the id: %s not found!", internshipInput.companyId))
        }
        internship.company = company.get()

        val areaOfInterest: Optional<AreaOfInterest> = areaOfInterestRepository.findById(internshipInput.categoryId!!)
        if (areaOfInterest.isEmpty) {
            throw java.util.NoSuchElementException(String.format("The AreaOfInteres with the id: %s not found!", internshipInput.categoryId))
        }
        internship.category = areaOfInterest.get()

        return internshipMapper.internshipToInternshipResult(
            internshipRepository.save(internship)
        )
    }

    override fun update(internshipInput: InternshipInput): InternshipResult? {
        val internship = internshipRepository.findById(internshipInput.id!!)
            .orElseThrow { IllegalArgumentException("Internship with id ${internshipInput.id} not found") }

        internship.title = internshipInput.title ?:  internship.title
        internship.shortDescription = internshipInput.shortDescription ?: internship.shortDescription
        internship.description = internshipInput.description ?: internship.shortDescription
        internship.active = internshipInput.active?: internship.active

        internshipInput.companyId?.let { companyId ->
            val company = companyRepository.findById(companyId)
                .orElseThrow { IllegalArgumentException("Company with id ${companyId} not found") }
            internship.company = company
        }

        internshipInput.categoryId?.let { categoryId ->
            val category = areaOfInterestRepository.findById(categoryId)
                .orElseThrow { IllegalArgumentException("Category with id ${categoryId} not found") }
            internship.category = category
        }

        return internshipMapper.internshipToInternshipResult(
            internshipRepository.save(internship)
        )
    }

    /*
    * filtered by interests and active and they are not in application
    * So, the student has not applied to those internships
    * */
    override fun getFilteredInternships(studentId: Long): List<InternshipResult>? {
        val student: Optional<Student> = studentRepository.findById(studentId)
        if (student.isEmpty) {
            throw NoSuchElementException(String.format("The Student with the id: %s not found!", studentId))
        }

        return internshipMapper.internshipToInternshipResult(
            internshipRepository.findRelevantInternshipsForStudent(student.get().areaOfInterestList, student.get().id!!)

        )
    }

    override fun findAllByCompanyId(companyId: Long): List<InternshipResult>? {
        val company: Optional<Company> = companyRepository.findById(companyId)
        if (company.isEmpty){
            throw NoSuchElementException(String.format("The Compnay with the id: %s not found!", companyId))
        }
        return internshipMapper.internshipToInternshipResult(
            internshipRepository.findAllByCompanyId(company.get().id!!)
        )
    }

    override fun deleteById(internshipId: Long) {
        if (!internshipRepository.findById(internshipId).isEmpty){
            internshipRepository.deleteById(internshipId)
        } else {
            throw java.util.NoSuchElementException(String.format("The Task with the id: %s not found!", internshipId))
        }
    }
}

// ----------------------------------------------------------------------------------------

interface RoleService {
    fun findAll(): List<RoleDetails>?
    fun findById(id: Long): RoleDetails?
    fun findByName(name: String): RoleDetails?
    fun create(roleDetails: RoleDetails): RoleDetails?
    fun update(roleDetails: RoleDetails): RoleDetails?
    fun deleteById(id: Long)
}
@Service
class AbstractRoleService(
    @Autowired
    val roleRepository: RoleRepository,

    @Autowired
    val roleMapper: RoleMapper
) : RoleService {
    override fun findAll(): List<RoleDetails>? {
        return roleMapper.roleToRoleDetailsList(
            roleRepository.findAll()
        )
    }

    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): RoleDetails? {
        val role: Optional<Role> = roleRepository.findById(id)

        if(role.isEmpty) {
            throw NoSuchElementException(String.format("There is no role with the id: %s.", id))
        }

        return roleMapper.roleToRoleDetails(
            role.get()
        )
    }

    @Throws(NoSuchElementException::class)
    override fun findByName(name: String): RoleDetails? {
        val roleType = try {
            RoleType.valueOf(name.uppercase())
        } catch (e: IllegalArgumentException) {
            throw NoSuchElementException("Invalid role name: $name")
        }

        val role = roleRepository.findByName(roleType.name)
            .orElseThrow { NoSuchElementException("Role not found with name: ${roleType.name}") }

        return roleMapper.roleToRoleDetails(role)
    }

    @Throws(IllegalStateException::class)
    override fun create(roleDetails: RoleDetails): RoleDetails? {
        val role: Role = roleMapper.roleDetailsToRole(roleDetails)

        return roleMapper.roleToRoleDetails(
            roleRepository.save(role)
        )
    }

    @Throws(NoSuchElementException::class)
    override fun update(roleDetails: RoleDetails): RoleDetails? {
        val role: Role = roleRepository.findById(roleDetails.id!!)
            .orElseThrow { NoSuchElementException(String.format("There is no role with the id: %s", roleDetails.id)) }

        role.name = roleDetails.name

        return roleMapper.roleToRoleDetails(
            roleRepository.save(role)
        )
    }

    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long) {
        val role: Role = roleRepository.findById(id)
            .orElseThrow { NoSuchElementException(String.format("There is no role with the id: %s", id)) }

        roleRepository.delete(role)
    }
}

interface RolePrivilegeService {
    fun findAll(): RolePrivilegeResult
    fun findById(id: Long): RolePrivilegeResult
}
@Service
class AbstractRolePrivilegeService(
    @Autowired
    val roleRepository: RoleRepository,

    @Autowired
    val rolePrivilegeMapper: RolePrivilegeMapper
) : RolePrivilegeService {
    override fun findAll(): RolePrivilegeResult {
        val roles: List<Role> = roleRepository.findAll()

        return rolePrivilegeMapper.rolePrivilegeResultMapping(
            roles
        )
    }

    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): RolePrivilegeResult {
        val role: Role = roleRepository.findById(id)
            .orElseThrow { NoSuchElementException(String.format("There is no role with the id: %s", id)) }

        return rolePrivilegeMapper.rolePrivilegeResultMapping(
            listOf(role)
        )
    }
}

// ----------------------------------------------------------------------------------------

interface PrivilegeService {
    fun findAll(): List<PrivilegeDetails>?
    fun findById(id: Long): PrivilegeDetails?
    fun create(privilegeDetails: PrivilegeDetails): PrivilegeDetails?
    fun update(privilegeDetails: PrivilegeDetails): PrivilegeDetails?
    fun deleteById(id: Long)
}
@Service
class AbstractPrivilegeService(
    @Autowired
    val privilegeRepository: PrivilegeRepository,

    @Autowired
    val privilegeMapper: PrivilegeMapper
) : PrivilegeService {
    override fun findAll(): List<PrivilegeDetails>? {
        return privilegeMapper.privilegeToPrivilegeDetailsList(
            privilegeRepository.findAll()
        )
    }

    @Throws(NoSuchElementException::class)
    override fun findById(id: Long): PrivilegeDetails? {
        val privilege: Optional<Privilege> = privilegeRepository.findById(id)

        if(privilege.isEmpty) {
            throw NoSuchElementException(String.format("There is no privilege with the id: %s"))
        }

        return privilegeMapper.privilegeToPrivilegeDetails(
            privilege.get()
        )
    }

    @Throws(IllegalStateException::class)
    override fun create(privilegeDetails: PrivilegeDetails): PrivilegeDetails? {
        val privilege: Privilege = privilegeMapper.privilegeDetails(privilegeDetails)

        return privilegeMapper.privilegeToPrivilegeDetails(
            privilegeRepository.save(privilege)
        )
    }

    @Throws(NoSuchElementException::class)
    override fun update(privilegeDetails: PrivilegeDetails): PrivilegeDetails? {
        val privilege: Privilege = privilegeRepository.findById(privilegeDetails.id!!)
            .orElseThrow() { NoSuchElementException(String().format("There is no privilege with the id: %2", privilegeDetails.id)) }

        privilege.name = privilegeDetails.name

        return privilegeMapper.privilegeToPrivilegeDetails(
            privilegeRepository.save(privilege)
        )
    }

    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long) {
        val privilege: Privilege = privilegeRepository.findById(id)
            .orElseThrow() { NoSuchElementException(String.format("There is no privilege with the id: %s", id)) }

        privilegeRepository.delete(privilege)
    }
}
// ----------------------------------------------------------------------------------------

interface CountryService{
    fun findAll(): List<CountryDetails>?
}

@Service
class AbstractCountryService(
    @Autowired
    val countryRepository: CountryRepository,
    @Autowired
    val countryMapper: CountryMapper
) : CountryService{
    override fun findAll(): List<CountryDetails>? {
        return countryMapper.countryListToCountryDetailsList(
            countryRepository.findAll()
        )
    }
}

interface GenderService{
    fun findAll(): List<GenderDetails>?
}

@Service
class AbstractGenderService(
    @Autowired
    val genderRepository: GenderRepository,
    @Autowired
    val genderMapper: GenderMapper
):GenderService {
    override fun findAll(): List<GenderDetails>? {
        return genderMapper.genderListToGenderDetailsList(
            genderRepository.findAll()
        )
    }
}

interface SkillService{
    fun findAll(): List<SkillDetails>?
}
@Service
class AbstractSkillService(
    @Autowired
    val skillRepository: SkillRepository,
    @Autowired
    val skillMapper: SkillMapper
): SkillService{
    override fun findAll(): List<SkillDetails>? {
        return skillMapper.skillListToSkillDetailsList(
            skillRepository.findAll()
        )
    }
}

//-----------------------------------------------------------------------------------------
interface SectorService{
    fun findAll(): List<SectorDetails>?
}
@Service
class AbstractSectorService(
    @Autowired
    val sectorRepository: SectorRepository,
    @Autowired
    val sectorMapper: SectorMapper
): SectorService{
    override fun findAll(): List<SectorDetails>? {
        return sectorMapper.sectorToSectorDetailsList(
            sectorRepository.findAll()
        )
    }
}

//-----------------------------------------------------------------------------------------
interface ApplicationService {
    fun findAll(): List<ApplicationResult>?
    fun findById(id: Long): ApplicationResult?
    fun findAllByStudentId(studentId: Long):List<ApplicationResult>
    fun create(applicationInput: ApplicationInput): ApplicationResult?
    fun update(applicationInput:ApplicationInput): ApplicationResult?
    fun deleteById(id: Long)
    fun getStudentsAndInternshipsByCompany(companyId: Long): List<StudentInternshipResult>
}
@Service
class AbstractApplicationService(
    @Autowired
    val applicationRepository: ApplicationRepository,
    @Autowired
    val studentRepository: StudentRepository,
    @Autowired
    val internshipRepository: InternshipRepository,
    @Autowired
    val applicationStatusRepository: ApplicationStatusRepository,
    @Autowired
    val  applicationMapper: ApplicationMapper,
    @Autowired
    val studentInternshipMapper: StudentInternshipMapper

) : ApplicationService {
    override fun findAll(): List<ApplicationResult>? {
        return applicationMapper.applicationToApplicationListResult(
            applicationRepository.findAll()
        )
    }

    override fun findById(id: Long): ApplicationResult? {
        val application: Optional<Application> = applicationRepository.findById(id)
        if(application.isEmpty) {
            throw NoSuchElementException(String.format("The Application with the id: %s not found!", id))
        }
        return applicationMapper.applicationToApplicationResult(
            application.get()
        )
    }

    override fun findAllByStudentId(studentId: Long): List<ApplicationResult> {
        return applicationMapper.applicationToApplicationListResult(
            applicationRepository.findAllByStudentId(studentId)
        )
    }

    override fun create(applicationInput: ApplicationInput): ApplicationResult {

        val studentId = applicationInput.studentId
            ?: throw IllegalArgumentException("Student ID cannot be null")
        val student = studentRepository.findById(studentId)
            .orElseThrow { NoSuchElementException("Student with id $studentId not found") }

        val internship = internshipRepository.findById(applicationInput.internshipId!!)
            .orElseThrow { NoSuchElementException("Internship with id ${applicationInput.internshipId} not found") }

        val status = applicationStatusRepository.findById(applicationInput.statusId!!)
            .orElseThrow { NoSuchElementException("Status with id ${applicationInput.statusId} not found") }

        val application = Application(
            student = student,
            internship = internship,
            status = status,
            // otros campos que vienen en applicationInput
        )

        return applicationMapper.applicationToApplicationResult(applicationRepository.save(application))
    }

    @Throws(NoSuchElementException::class)
    override fun update(applicationInput: ApplicationInput): ApplicationResult? {


        val application = applicationRepository.findById(applicationInput.id!!)
            .orElseThrow{ NoSuchElementException("Application with id ${applicationInput.id} not found") }

        applicationInput.studentId?.let { studenId ->
            val student = studentRepository.findById(applicationInput.studentId).orElseThrow {
                NoSuchElementException("Student with id ${applicationInput.studentId} not found")
            }
            application.student = student

        }
        applicationInput.internshipId?.let { studenId ->
            val internship = internshipRepository.findById(applicationInput.internshipId).orElseThrow {
                NoSuchElementException("Internship with id ${applicationInput.internshipId} not found")
            }
            application.internship = internship
        }

        applicationInput.statusId?.let { studenId ->
            val status = applicationStatusRepository.findById(applicationInput.statusId).orElseThrow {
                NoSuchElementException("ApplicationStatus with id ${applicationInput.statusId} not found")
            }
            application.status = status
        }

        return applicationMapper.applicationToApplicationResult(
            applicationRepository.save(application)
        )
    }

    @Throws(NoSuchElementException::class)
    override fun deleteById(id: Long) {
        if(!applicationRepository.findById(id).isEmpty) {
            applicationRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Application with id $id not found")
        }

    }

    override fun getStudentsAndInternshipsByCompany(companyId: Long): List<StudentInternshipResult> {

        return studentInternshipMapper.studentInternshipToStudentInternshipListResult(applicationRepository.findStudentsAndInternshipsByCompanyId(companyId))
    }
}


interface InterviewService{
    fun getInterview(intershipId: Long): InterviewAI
}


@Service
class AbstractInterviewService(

    private val internshipRepository: InternshipRepository,

    private val aiService: AIService


): InterviewService {

    override fun getInterview(internshipId: Long): InterviewAI {
        val internship: Optional<Internship> = internshipRepository.findById(internshipId)


        val systemText = """
        You are an AI specialized in generating interview simulations for internship positions.
        
        Your task is to generate a structured interview in **JSON format only**, using **lowercase property names** exactly as shown below.
        
        Please follow this JSON structure strictly:
        
        {
          "introduction": { "questions": ["..."] },
          "knowledge_assessment": { "questions": ["..."] },
          "problem_solving": { "exercises": ["..."] },
          "expectations_and_closure": { "questions": ["..."] }
        }
        
        Important rules:
        - Do not include any explanations, introductions, or formatting outside the JSON.
        - Use only the exact lowercase keys: "introduction", "knowledge_assessment", "problem_solving", "expectations_and_closure", "questions", and "exercises".
        - Do not use uppercase or camelCase.
        - Return only valid JSON, no markdown formatting.
        - The questions for each section should vary in difficulty and cover both soft and hard skills.
        - Provide at least three questions or exercises per section.
        - The text must be in Spanish, only the JSON structure must be in English.
        Generate questions and exercises suitable for a candidate applying to the following internship:
        """.trimIndent()

        val userText = """
                    Internship Title: ${internship.get().title}
                    Company: ${internship.get().company}
                    Description: ${internship.get().description}
                   
                """.trimIndent()

        // Esto configura Jackson para que convierta snake_case a camelCase automsticamente.
        val objectMapper: ObjectMapper = jacksonObjectMapper()
        objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

        val jsonResponse = aiService.getOpenAiResponseWithSystemPrompt(systemText, userText)
            ?: throw IllegalStateException("AI response was null")
        println("JSON devuelto por la IA:\n$jsonResponse")
        if (!jsonResponse.trim().endsWith("}")) {
            throw IllegalStateException("La respuesta de la IA está incompleta. No se puede parsear.")
        }


        return objectMapper.readValue(jsonResponse, InterviewAI::class.java)
    }
}


/*
* The TikaDocumentReader uses Apache Tika to extract text from a variety of document formats,
*  such as PDF,
* DOC/DOCX, PPT/PPTX, and HTML. For a comprehensive list of supported formats.
*
* */

@Service
class DocumentReaderService {

    fun loadPlainTextFromResource(resource: Resource): String {
        val reader = TikaDocumentReader(resource)
        return reader.read().joinToString("\n") { it.text }
    }
}

interface AnalizeCVService{
    fun getAnalizedCurriculum(resource: Resource): AnalizeCurriculumAI
}

@Service
class AbstractAnalizeCVService(
    private val aiService: AIService,
    private val documentReaderService: DocumentReaderService
) : AnalizeCVService {

    override fun getAnalizedCurriculum(resource: Resource): AnalizeCurriculumAI {
        val systemText = """
            You are an AI specialized in analyzing resumes (CVs) for internship candidates.

            Analyze the content and extract insights using **strictly this JSON format** (camel case only):
            {
              "strengths": ["..."],
              "weaknesses": ["..."],
              "improvementAreas": ["..."],
              "overallAssessment": "..."
            }

            Important rules:
            - Do NOT include explanations or formatting outside the JSON.
            - Use ONLY the exact lowercase property names: "strengths", "weaknesses", "improvement_areas", "overall_assessment".
            - Return only valid JSON. No markdown or extra commentary.
            - The text must be in Spanish, only the JSON structure must be in English.
        """.trimIndent()

        // Esto configura Jackson para que convierta snake_case a camelCase automsticamente.
        //val objectMapper: ObjectMapper = jacksonObjectMapper()
        //objectMapper.propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE

        val content = documentReaderService.loadPlainTextFromResource(resource)

        val jsonResponse = aiService.getAnthropicChatModelWithSystemPrompt(systemText, content)
            ?: throw IllegalStateException("AI response was null")
        println("AI JSON Response:\n$jsonResponse")

        if (!jsonResponse.contains("\"strengths\"") || !jsonResponse.contains("\"weaknesses\"")) {
            throw IllegalStateException("La respuesta de la IA no contiene las claves requeridas.")
        }

        if (!jsonResponse.trim().endsWith("}")) {
            throw IllegalStateException("La respuesta de la IA está incompleta. No se puede parsear.")
        }

        return jacksonObjectMapper().readValue(jsonResponse, AnalizeCurriculumAI::class.java)
    }
}



@Service
class AIService(
    private val openAiChatModel: OpenAiChatModel,
    private val anthropicChatModel: AnthropicChatModel
) {


    fun getAnthropicChatModelWithSystemPrompt(systemText: String, userText: String): String? {
        return getResponseWithSystemPrompt(systemText, userText, anthropicChatModel)
    }

    fun getOpenAiResponseWithSystemPrompt(systemText: String, userText: String): String? {
        return getResponseWithSystemPrompt(systemText, userText, openAiChatModel)
    }

    private fun getResponseWithSystemPrompt(
        systemText: String,
        userText: String,
        model: ChatModel
    ): String? {
        val chatClient = ChatClient.builder(model).build()

        val systemMessage = SystemMessage(systemText)
        val userMessage = UserMessage(userText)
        val prompt = Prompt(listOf(systemMessage, userMessage))

        return try {
            chatClient.prompt(prompt)
                .call()
                .content()
        } catch (e: Exception) {
            handleError(e)
        }
    }

    private fun handleError(e: Exception): String {
        println("Error calling AI service: ${e.message}")
        return "Sorry, there was an error processing your request."
    }
}



@ConfigurationProperties(prefix = "cloudinary")
data class CloudinaryProperties(
    val cloudName: String,
    val apiKey: String,
    val apiSecret: String
)

@Configuration
class CloudinaryConfig {

    @Bean
    fun cloudinary(cloudinaryProperties: CloudinaryProperties): Cloudinary {
        val config = mapOf(
            "cloud_name" to cloudinaryProperties.cloudName,
            "api_key" to cloudinaryProperties.apiKey,
            "api_secret" to cloudinaryProperties.apiSecret,
            "secure" to true
        )
        return Cloudinary(config)
    }
}

@Service
class CloudinaryService(private val cloudinary: Cloudinary) {

    fun uploadFile(file: MultipartFile): Map<*, *> {
        return try {
            val uploadResult = cloudinary.uploader().upload(file.bytes, ObjectUtils.emptyMap())
            uploadResult
        } catch (e: IOException) {
            throw RuntimeException("Error al subir el archivo a Cloudinary", e)
        }
    }

    fun deleteFile(publicId: String): Map<*, *> {
        return try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap())
        } catch (e: IOException) {
            throw RuntimeException("Error al eliminar el archivo de Cloudinary", e)
        }
    }

    fun getFileUrl(publicId: String): String {
        return cloudinary.url().generate(publicId)
    }

}

@Service
@Transactional
class AppUserDetailsService (
    @Autowired
    val userRepository: UserRepository,
    @Autowired
    val roleRepository: RoleRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user: User = userRepository.findByEmail(username).orElseThrow {
            UsernameNotFoundException("User with email $username not found")
        }

        return org.springframework.security.core.userdetails.User(

            user.email,
            user.password,
            user.enabled!!,
            true,
            true,
            true,
            getAuthorities(user.roleList)
        )
    }


    private fun getAuthorities(roles: Collection<Role>): Collection<GrantedAuthority> {
        return roles.flatMap { role ->
            sequenceOf(SimpleGrantedAuthority(role.name)) +
                    role.privilegeList.map { privilege -> SimpleGrantedAuthority(privilege.name) }
        }
    }
}