package com.example.oportunia.data.di

import com.example.oportunia.data.remote.api.AuthService
import com.example.oportunia.data.repository.ApplicationRepositoryImpl
import com.example.oportunia.data.repository.AreaOfInterestRepositoryImpl
import com.example.oportunia.data.repository.CVRepositoryImpl
import com.example.oportunia.data.repository.CompanyRepositoryImpl
import com.example.oportunia.data.repository.CountryRepositoryImpl
import com.example.oportunia.data.repository.GenderRepositoryImpl
import com.example.oportunia.data.repository.InternshipRepositoryImpl
import com.example.oportunia.data.repository.InterviewServiceImpl
import com.example.oportunia.data.repository.SectorRepositoryImpl
import com.example.oportunia.data.repository.SkillRepositoryImpl
import com.example.oportunia.data.repository.StudentRepositoryImpl
import com.example.oportunia.data.repository.UserRepositoryImpl
import com.example.oportunia.domain.repository.ApplicationRepository
import com.example.oportunia.domain.repository.AreaOfInterestRepository
import com.example.oportunia.domain.repository.CVRepository
import com.example.oportunia.domain.repository.CompanyRepository
import com.example.oportunia.domain.repository.CountryRepository
import com.example.oportunia.domain.repository.GenderRepository
import com.example.oportunia.domain.repository.InternshipRepository
import com.example.oportunia.domain.repository.InterviewRepository
import com.example.oportunia.domain.repository.SectorRepository
import com.example.oportunia.domain.repository.SkillRepository
import com.example.oportunia.domain.repository.StudentRepository
import com.example.oportunia.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
/**
 * Dagger Hilt module that provides repository dependencies for the application.
 *
 * This module uses [Binds] to map implementation classes to their interfaces,
 * allowing Hilt to inject the correct implementation when an interface type is requested.
 * All bindings are scoped to [SingletonComponent] to ensure a single instance is shared
 * throughout the application's lifecycle.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Binds the concrete implementation [InternshipRepositoryImpl] to the [InternshipRepository] interface.
     *
     * @param InternshipRepositoryImpl The implementation instance to be provided when [InternshipRepository] is requested
     * @return The bound [InternshipRepository] interface
     */

    @Binds
    @Singleton
    abstract fun bindInternshipRepository(
        internshipRepositoryImpl: InternshipRepositoryImpl
    ): InternshipRepository


    @Binds
    @Singleton
    abstract fun bindStudentRepository(
        studentRepositoryImpl: StudentRepositoryImpl
    ): StudentRepository

    @Binds
    @Singleton
    abstract fun bindInterviewRepository(
        interviewRepositoryImpl: InterviewServiceImpl,
    ): InterviewRepository


    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindApplicationRepository(
        applicationRepositoryImpl: ApplicationRepositoryImpl,
    ): ApplicationRepository

    @Binds
    @Singleton
    abstract fun bindCVRepository(
        cVRepositoryImpl: CVRepositoryImpl
    ): CVRepository

    @Binds
    @Singleton
    abstract fun bindCompanyRepository(
        companyRepositoryImpl: CompanyRepositoryImpl
    ): CompanyRepository

    @Binds
    @Singleton
    abstract fun bindCountryRepository(
        countryReposito: CountryRepositoryImpl
    ): CountryRepository

    @Binds
    @Singleton
    abstract fun bindAreaOfInterestRepository(
        areaOfInterestRepositoryImpl: AreaOfInterestRepositoryImpl
    ): AreaOfInterestRepository

    @Binds
    @Singleton
    abstract fun bindGenderRepository(
        genderRepositoryImpl: GenderRepositoryImpl
    ): GenderRepository

    @Binds
    @Singleton
    abstract fun bindSkillRepository(
        skillRepositoryImpl: SkillRepositoryImpl
    ): SkillRepository

    @Binds
    @Singleton
    abstract fun bindSectorRepository(
        sectorRepositoryImpl: SectorRepositoryImpl
    ) : SectorRepository


}


