package edu.backend.oportuniapp

import org.mapstruct.*
import org.springframework.stereotype.Component

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface UserMapper {
    fun userToUserResult(user: User) : UserResult

    fun userToUserResultList(users: List<User>): List<UserResult>

    fun userInputToUser(userInput: UserInput): User
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", uses = [SkillMapper::class], unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentMapper {
    fun studentToStudentResult(student: Student): StudentResult

    fun studentToStudentListResult(students: List<Student>): List<StudentResult>
    @Mapping(target = "country", ignore = true) //ignorar para despues buscar en la db o asignar en create or update
    @Mapping(target = "profilePicUrl", ignore = true)
    fun studentInputToStudent(studentInput: StudentInput): Student

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun studentInputToStudent(dto: StudentInput, @MappingTarget student: Student)
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", uses = [SectorMapper::class], unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface CompanyMapper {
    fun companyToCompanyResult(company: Company): CompanyResult

    fun companyToCompanyListResult(company: List<Company>): List<CompanyResult>

    fun companyToCompanySimpleResult(company: Company): CompanySimpleResult

    @Mapping(target = "sectorList", ignore = true)
    @Mapping(target = "pictureUrl", ignore = true)
    fun companyInputToCompany(input: CompanyInput): Company

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun companyInputToCompany(dto: CompanyInput, @MappingTarget company: Company)
}
// ----------------------------------------------------------------------------------------
@Mapper(componentModel = "spring", uses = [SectorMapper::class], unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface StudentInternshipMapper {


    fun studentInternshipToStudentInternshipResult(studentInternship: StudentInternship): StudentInternshipResult

    fun studentInternshipToStudentInternshipListResult(studentInternship: List<StudentInternship>): List<StudentInternshipResult>

}
// ----------------------------------------------------------------------------------------
@Mapper(componentModel = "spring", uses = [SectorMapper::class], unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface AreaOfInterestMapper {


    fun areaOfInterestToAreaOfInterestDetails(areaOfInterest: AreaOfInterest): AreaOfInterestDetails

    fun areaOfInterestToAreaOfInterestDetailsListDetails(areaOfInterest: List<AreaOfInterest>): List<AreaOfInterestDetails>

}
// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface ApplicationMapper{
    fun applicationToApplicationResult(application: Application): ApplicationResult
    fun applicationToApplicationListResult(application: List<Application>): List<ApplicationResult>
    fun applicationInputToApplication(input: ApplicationInput): Application

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun applicationInputToApplication(input: ApplicationInput,  @MappingTarget application: Application)
}


// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring",  unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SectorMapper {
    fun sectorToSectorDetails(sector: Sector): SectorDetails
    fun sectorToSectorDetailsList(sectors: List<Sector>): List<SectorDetails>
    fun sectorToSectorListResult(sectors: List<Sector>): List<SectorDetails>
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SkillMapper {
    fun skillToSkillDetails(skill: Skill): SkillDetails
    fun skillListToSkillDetailsList(skills: List<Skill>): List<SkillDetails>
}

// ----------------------------------------------------------------------------------------
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface CountryMapper {
    fun countryToCountryDetails(country: Country): CountryDetails
    fun countryListToCountryDetailsList(country: List<Country>): List<CountryDetails>
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface GenderMapper {
    fun  genderToGenderDetails(gender: Gender): GenderDetails
    fun genderListToGenderDetailsList(skills: List<Gender>): List<GenderDetails>
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface InternshipMapper {
    fun internshipToInternshipResult(internship: Internship): InternshipResult

    fun internshipToInternshipResult(internship: List<Internship>): List<InternshipResult>

    @Mapping(target = "company", ignore = true)
    @Mapping(target = "aplicationList", ignore = true)
    fun internshipInputToInternship(internshipInput: InternshipInput): Internship

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    fun internshipInputToInternship(dto: InternshipInput, @MappingTarget internship: Internship)
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface RoleMapper {
    fun roleToRoleDetails(role: Role): RoleDetails

    fun roleToRoleDetailsList(role: List<Role>): List<RoleDetails>

    fun roleDetailsToRole(roleDetails: RoleDetails): Role
}

// ----------------------------------------------------------------------------------------

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
interface PrivilegeMapper {
    fun privilegeToPrivilegeDetails(privilege: Privilege): PrivilegeDetails

    fun privilegeToPrivilegeDetailsList(privilege: List<Privilege>): List<PrivilegeDetails>

    fun privilegeDetails(privilegeDetails: PrivilegeDetails): Privilege
}


// ----------------------------------------------------------------------------------------
// ---------------------------------- CUSTOM MAPPERS --------------------------------------
// ----------------------------------------------------------------------------------------

// Further testing needed
@Component
class RolePrivilegeMapper(
    private val roleMapper: RoleMapper,

    private val privilegeMapper: PrivilegeMapper
) {
    fun rolePrivilegeResultMapping(roles: List<Role>): RolePrivilegeResult {

        val roleInfo: Map<Long, RoleDetails> = roles.associate { role ->
            role.id!! to roleMapper.roleToRoleDetails(role)
        }

        val privileges: List<Privilege> = roles
            .flatMap { it.privilegeList }
            .distinctBy { it.id }

        val privilegeInfo: Map<Long, PrivilegeDetails> = privileges.associate { privilege ->
            privilege.id!! to privilegeMapper.privilegeToPrivilegeDetails(privilege)
        }

        val rolesAndPrivilegesRelation: Map<Long, List<Long>> = roles.associate { role ->
            role.id!! to role.privilegeList.mapNotNull { it.id }
        }

        return RolePrivilegeResult(roleInfo, privilegeInfo, rolesAndPrivilegesRelation)
    }
}
