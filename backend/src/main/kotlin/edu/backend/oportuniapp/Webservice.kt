package edu.backend.oportuniapp

import org.springframework.core.io.InputStreamResource
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("\${url.users}")
class UserController(private val userService: UserService) {
    @GetMapping
    @ResponseBody
    fun findAll(): List<UserResult>? = userService.findAll()

    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: Long): UserResult? = userService.findById(id)

    @GetMapping("/username/{username}")
    @ResponseBody
    fun findById(@PathVariable username: String): UserResult? = userService.findByEmail(username)

    @PostMapping("register", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody userInput: UserInput) : UserResult? = userService.create(userInput)

    @PutMapping("update", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun update(@RequestBody userInput: UserInput) : UserResult? = userService.update(userInput)

    @DeleteMapping("{id}")
    @ResponseBody
    fun delete(@PathVariable id: Long) = userService.deleteById(id)
}

@RestController
@RequestMapping("\${url.login}")
class authController(private val appUserDetailsService: AppUserDetailsService) {
    @PostMapping
    fun login(@RequestBody userInput: UserInput): UserDetails? {
        return appUserDetailsService.loadUserByUsername(userInput.email)
    }
}

// ----------------------------------------------------------------------------------------

@RestController
@RequestMapping("\${url.students}")
class StudentController(
    private val studentService: StudentService,
    private val userRoleService: UserRoleService,
    private val roleService: RoleService
) {
    @GetMapping
    @ResponseBody
    fun findAll() = studentService.findAll()

    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id:Long) = studentService.findById(id)

    @PostMapping("register", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody studentInput: StudentInput) : StudentResult? {
        val createdStudent: StudentResult? = studentService.create(studentInput)

        if (createdStudent != null) {
            userRoleService.assignRoleToUser(createdStudent.id, RoleType.ROLE_STUDENT.toString())
        }

        return createdStudent
    }

    @PutMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun updateStudent(
        @RequestPart("student") studentInput: StudentInput,
        @RequestPart("image", required = false) image: MultipartFile?
    ): StudentResult? {
        return studentService.update(studentInput, image)
    }

    @DeleteMapping("{id}")
    @ResponseBody
    fun deleteById(@PathVariable id: Long) {
          studentService.deleteById(id)
    }
}

// ----------------------------------------------------------------------------------------

@RestController
@RequestMapping("\${url.companies}")
class CompanyController(
    private val companyService: CompanyService,
    private val applicationService: ApplicationService,
    private val userRoleService: UserRoleService,
    private val roleService: RoleService
)  {
    @GetMapping
    @ResponseBody
    fun findAll() = companyService.findAll()

    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: Long) = companyService.findById(id)

    @PostMapping("register", consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody companyInput: CompanyInput) : CompanySimpleResult? {
        val createdCompany: CompanySimpleResult? = companyService.create(companyInput)

        if(createdCompany != null) {
            userRoleService.assignRoleToUser(createdCompany.id, RoleType.ROLE_COMPANY.toString())
        }

        return createdCompany
    }

    @PutMapping(
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun update(
        @RequestPart("company") companyInput: CompanyInput,
        @RequestPart("image", required = false) image: MultipartFile?
    ): CompanyResult? {
        return companyService.update(companyInput, image)
    }

    @DeleteMapping("{id}")
    @ResponseBody
    fun deleteById(@PathVariable id: Long) = companyService.deleteById(id)



}

// ----------------------------------------------------------------------------------------

@RestController
@RequestMapping("\${url.internships}")
class InternshipController(private val internshipService: InternshipService, private val areaOfInterestService: AreaOfInterestService) {
    @GetMapping
    @ResponseBody
    fun findAll() = internshipService.findAll()

    @GetMapping("{id}")
    @ResponseBody
    fun findById(@PathVariable id: Long) = internshipService.findById(id)

    @GetMapping("/student/{studentId}")
    @ResponseBody
    fun findByFilteredInternships(@PathVariable studentId: Long) = internshipService.getFilteredInternships(studentId)

    @GetMapping("/company/{companyId}")
    @ResponseBody
    fun findAllByCompanyId(@PathVariable companyId: Long) = internshipService.findAllByCompanyId(companyId)

    @GetMapping("/categories")
    fun getAllCategories() = areaOfInterestService.findAll()

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun create(@RequestBody internshipInput: InternshipInput) : InternshipResult? {
        return internshipService.create(internshipInput)
    }

    @PutMapping
    @ResponseBody
    fun update( @RequestBody internshipInput: InternshipInput): InternshipResult?  {
        return internshipService.update(internshipInput)
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    fun deleteById(@PathVariable id: Long) = internshipService.deleteById(id)
}


// ----------------------------------------------------------------------------------------

@RestController
@RequestMapping("\${url.applications}")
class ApplicationController(private val applicationService: ApplicationService) {

    @GetMapping
    fun findAll(): List<ApplicationResult>? {
        return applicationService.findAll()
    }

    //obtiene las aplicaciones por el id de estudiantes
    @GetMapping("/student/{studentId}")
    fun findAllByStudentId(@PathVariable studentId: Long): List<ApplicationResult>? {
        return applicationService.findAllByStudentId(studentId)
    }


    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ApplicationResult? {
        return applicationService.findById(id)
    }

    @GetMapping("/{companyId}/students-internships")
    @ResponseBody
    fun getStudentsAndInternshipsByCompany(@PathVariable companyId: Long) = applicationService.getStudentsAndInternshipsByCompany(companyId)

    @PostMapping
    fun create(@RequestBody applicationInput: ApplicationInput): ApplicationResult? {
        return applicationService.create(applicationInput)
    }
    @PutMapping()
    @ResponseBody
    fun update(@RequestBody applicationInput: ApplicationInput): ApplicationResult? {
        return applicationService.update(applicationInput)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) {
        applicationService.deleteById(id)
    }
}

// ----------------------------------------------------------------------------------------

@RestController
@RequestMapping("\${url.roles}")
class RoleController(private val roleService: RoleService) {

    @GetMapping
    fun findAll(): List<RoleDetails>? {
        return roleService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): RoleDetails? {
        return roleService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody roleDetails: RoleDetails): RoleDetails? {
        return roleService.create(roleDetails)
    }

    @PutMapping("update")
    fun update(@RequestBody roleDetails: RoleDetails): RoleDetails? {
        return roleService.update(roleDetails)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) {
        roleService.deleteById(id)
    }
}

// Further testing needed
@RestController
@RequestMapping("\${url.rolePrivs}")
class RolePrivilegeController(private val rolePrivilegeService: RolePrivilegeService) {

    @GetMapping
    fun findAll(): RolePrivilegeResult = rolePrivilegeService.findAll()

    @GetMapping("{id}")
    fun findById(@PathVariable id: Long): RolePrivilegeResult = rolePrivilegeService.findById(id)
}

// ----------------------------------------------------------------------------------------

@RestController
@RequestMapping("\${url.privileges}")
class PrivilegeController(private val privilegeService: PrivilegeService) {

    @GetMapping
    fun findAll(): List<PrivilegeDetails>? {
        return privilegeService.findAll()
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): PrivilegeDetails? {
        return privilegeService.findById(id)
    }

    @PostMapping
    fun create(@RequestBody privilegeDetails: PrivilegeDetails): PrivilegeDetails? {
        return privilegeService.create(privilegeDetails)
    }

    @PutMapping("update")
    fun update(@RequestBody privilegeDetails: PrivilegeDetails): PrivilegeDetails? {
        return privilegeService.update(privilegeDetails)
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long) {
        privilegeService.deleteById(id)
    }
}

@RestController
@RequestMapping("\${url.interviews}")
class InterviewController(private val interviewService: InterviewService) {

    @GetMapping("/{internshipId}")
    fun getInterview(@PathVariable internshipId: Long): InterviewAI {
        return interviewService.getInterview(internshipId)
    }
}





@RestController
@RequestMapping("\${url.curriculums}")
class AnalizeCurriculumController(private val abstractAnalizeCVService: AbstractAnalizeCVService) {
    @PostMapping(
        "/upload",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun uploadDocument(@RequestParam("file") file: MultipartFile):  AnalizeCurriculumAI {
        val resource = InputStreamResource(file.inputStream)
        return abstractAnalizeCVService.getAnalizedCurriculum(resource)
    }

}

@RestController
@RequestMapping("\${url.countries}")
class CountryController(private val countryService: CountryService) {
    @GetMapping
    fun getCountries(): List<CountryDetails>? {
        return countryService.findAll()
    }
}

@RestController
@RequestMapping("\${url.areasofinterest}")
class AreaOfInterestController(private val areaOfInterestService: AreaOfInterestService) {
    @GetMapping
    fun getAreaOfInterest(): List<AreaOfInterestDetails>? {
        return areaOfInterestService.findAll()
    }

}

@RestController
@RequestMapping("\${url.genders}")
class GenderController(private val genderService: GenderService) {
    @GetMapping
    fun getGenders(): List<GenderDetails>? {
        return genderService.findAll()
    }

}
@RestController
@RequestMapping("\${url.skills}")
class SkillController(private val skillService: SkillService) {
    @GetMapping
    fun getSkills(): List<SkillDetails>? {
        return skillService.findAll()
    }
}

@RestController
@RequestMapping("\${url.sectors}")
class SectorController(private val sectorService: SectorService) {
    @GetMapping
    fun getSectors(): List<SectorDetails>? {
        return sectorService.findAll()
    }
}


