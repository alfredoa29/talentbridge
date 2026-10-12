package edu.backend.oportuniapp

data class UserInput(
    var id: Long? = null,
    var email: String = "",
    var password: String = "",
    var enabled: Boolean? = true,
    var tokenExpired: Boolean? = false
)

data class UserResult(
    var id: Long,
    var email: String,
    var enabled: Boolean?,
    var tokenExpired: Boolean?
)

// -------------------------------------------------------------------------------

data class  GenderDetails(
    var id : Int,
    var name : String
)

// -------------------------------------------------------------------------------

data class  SkillDetails(
    var id: Long,
    var name: String,
)

// -------------------------------------------------------------------------------

data class SectorDetails(
    var id: Long,
    var name: String,
)

// -------------------------------------------------------------------------------

data class  AreaOfInterestDetails(
    var id : Int,
    var name: String,
)

// -------------------------------------------------------------------------------
data class CountryDetails(
    var id: Long,
    var name: String,
)
// -------------------------------------------------------------------------------
data class StudentInput(
    var email: String? = null,
    var password: String? = null,
    var id: Long? = null,
    var fullName: String? = null,
    var username: String? = null,
    var university: String? = null,
    var career: String? = null,
    var profilePicUrl: String? = null,
    var countryId: Long? = null,
    var genderId:  Long? = null,
    var skillIds: List<Long>? = null,  // Solo los IDs de las habilidades seleccionadas
    var areaOfInterestIds: List<Long>? = null,  // Solo los IDs de las áreas de interés seleccionadas
)

data class StudentResult(
    var id: Long,
    var fullName: String,
    var university: String,
    var career: String,
    var username: String,
    var email: String,
    var skillList: List<SkillDetails>? = null,
    var areaOfInterestList: List<AreaOfInterestDetails>? = null,
    var profilePicUrl: String,
    var country: CountryDetails,
    var gender: GenderDetails,
)

data class StudentInternshipResult(
    val applicationId: Long,
    val studentId: Long,
    val studentName: String,
    var profilePicUrl: String,
    val internshipId: Long,
    val internshipTitle: String,
    val companyName: String,

)

// -------------------------------------------------------------------------------

data class  CompanyInput(
    var email: String? = null,
    var password: String? = null,
    var id: Long?=null,
    var name: String? = null,
    var website: String? = null,
    var pictureUrl: String? = null,
    var sectorListIds: List<Long>? = null,
)

data class CompanyBasicResult(
    var id: Long,
    var name: String,
    var pictureUrl: String,
)

data class CompanySimpleResult(
    var id: Long,
    var name: String,
    var website: String,
    var pictureUrl: String,
    var sectorList: List<SectorDetails>? = null
)

data class CompanyResult(
    var id: Long,
    var name: String,
    var website: String,
    var pictureUrl: String,
    var sectorList: List<SectorDetails>? = null,
    var internshipList: List<InternshipResult>? = null
)

// -------------------------------------------------------------------------------

data class InternshipInput(
    var id: Long? = null,
    var title: String? = null,
    var shortDescription: String? = null,
    var description: String? = null,
    var companyId:  Long? = null,
    var active: Boolean? = null,
    var categoryId: Long? = null,
)

data class InternshipResult(
    var id: Long,
    var title: String,
    var shortDescription: String,
    var description: String,
    var company: CompanyBasicResult,
    var active: Boolean,
    var category: AreaOfInterestDetails

)

// -------------------------------------------------------------------------------

data class PrivilegeDetails(
    var id: Long? = null,
    var name: String
)

data class RolePrivilegeResult(
    var roleInfo: Map<Long, RoleDetails>,
    var privilegeInfo: Map<Long, PrivilegeDetails>,
    var rolePrivilegeLinks: Map<Long, List<Long>>
)

// -------------------------------------------------------------------------------

enum class RoleType{
    ROLE_ADMIN,
    ROLE_COMPANY,
    ROLE_STUDENT
}

data class RoleDetails(
    var id: Long? = null,
    var name: String,
)

data class UserRoleInput(
    var userId: Long? = null,
    var roleId: Long? = null
)

data class UserRoleResult(
    var userInfo: Map<Long, UserResult>,
    var roleInfo: Map<Long, RoleDetails>,
    var userRoleLinks: Map<Long, List<Long>>
)

// -------------------------------------------------------------------------------

data class  ApplicationInput(
    var id: Long? = null,
    var internshipId: Long? = null,
    var studentId: Long? = null,
    var statusId: Long? = null,
)

data class ApplicationResult(
    var id: Long,
    var status: ApplicationStatusDetails,
    var student: StudentResult,
    var internship: InternshipResult
)

data class ApplicationStatusDetails(
    var id: Long,
    var name: String
)