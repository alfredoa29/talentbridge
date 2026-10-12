package com.example.oportunia.data.mapper

import com.example.oportunia.data.remote.dto.ApplicationDto
import com.example.oportunia.data.remote.dto.ApplicationStatusDto
import com.example.oportunia.data.remote.dto.CreateApplicationDto
import com.example.oportunia.data.remote.dto.StudentInternshipDto
import com.example.oportunia.data.remote.dto.ApplicationUpdateDto
import com.example.oportunia.domain.model.Application
import com.example.oportunia.domain.model.ApplicationStatus
import com.example.oportunia.domain.model.CreateApplication
import com.example.oportunia.domain.model.StudentInternship
import com.example.oportunia.domain.model.ApplicationStatusChange
import javax.inject.Inject

class ApplicationMapper @Inject constructor(
    private val internshipMapper: InternshipMapper,
    private val studentMapper: StudentMapper
) {
    fun mapToDomain(dto: ApplicationDto): Application {
        return Application(
            id = dto.id,
            internship = internshipMapper.mapToDomain(dto.internship ?: throw IllegalArgumentException("Internship is required")),
            student = studentMapper.mapToDomain(dto.student ?: throw IllegalArgumentException("Student is required")),
            status = mapToDomain(dto.status)
        )
    }

    fun mapToDomainList(dtoList: List<ApplicationDto>): List<Application> {
        return dtoList.map { mapToDomain(it) }
    }

    fun mapToDto(app: Application): ApplicationDto {
        return ApplicationDto(
            id = app.id,
            internship = internshipMapper.mapToDto(app.internship),
            student = studentMapper.mapToDto(app.student),
            status = mapToDto(app.status) //este metodo es maptoDto para ApplicationStatusDto que esta aqui mismo
        )
    }

    fun mapCreateApplicationToDto(createApplication: CreateApplication): CreateApplicationDto {
        return CreateApplicationDto(
            internshipId = createApplication.internshipId,
            studentId = createApplication.studentId,
            statusId = createApplication.statusId
        )
    }

    fun mapCreateApplicationDtoToDomain(dto: CreateApplicationDto): CreateApplication {
        return CreateApplication(
            internshipId = dto.internshipId,
            studentId = dto.studentId,
            statusId = dto.statusId
        )
    }

    fun mapToDomain(applicationStatusDto: ApplicationStatusDto): ApplicationStatus {
        return ApplicationStatus(
            id = applicationStatusDto.id,
            name = applicationStatusDto.name
        )
    }
    fun mapToDto(appStatus: ApplicationStatus): ApplicationStatusDto {
        return ApplicationStatusDto(
            id = appStatus.id,
            name = appStatus.name
        )
    }

    fun mapToDomain(applicationupdateStatusDto: ApplicationUpdateDto): ApplicationStatusChange {
        return ApplicationStatusChange(
            applicationId = applicationupdateStatusDto.id,
            statusId = applicationupdateStatusDto.statusId
        )
    }

    fun mapToDto(applicationStatusChange: ApplicationStatusChange): ApplicationUpdateDto {
        return ApplicationUpdateDto(
            id = applicationStatusChange.applicationId,
            statusId = applicationStatusChange.statusId

        )
    }

    fun mapStudentInternshipToDomain(studentInternshipDto: StudentInternshipDto): StudentInternship {
        return StudentInternship(
            applicationId = studentInternshipDto.applicationId,
            studentId = studentInternshipDto.internshipId,
            studentName = studentInternshipDto.studentName,
            profilePicUrl = studentInternshipDto.profilePicUrl,
            internshipId = studentInternshipDto.internshipId,
            companyName = studentInternshipDto.companyName,
            internshipTitle = studentInternshipDto.internshipTitle

        )
    }

    fun mapStudentInternshipToDomainList(dtoList: List<StudentInternshipDto>): List<StudentInternship>{
        return dtoList.map { mapStudentInternshipToDomain(it) }

    }
}
