package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.CompanyMapper
import com.example.oportunia.data.remote.CompanyRemoteDataSource
import com.example.oportunia.data.remote.dto.CompanyInput
import com.example.oportunia.data.remote.dto.CompanyUpdateDto
import com.example.oportunia.domain.model.Company
import com.example.oportunia.domain.repository.CompanyRepository
import java.io.File
import javax.inject.Inject

class CompanyRepositoryImpl @Inject constructor(
    private val remoteDataSource: CompanyRemoteDataSource,
    private val companyMapper: CompanyMapper
) : CompanyRepository {

    override suspend fun findCompanyById(companyId: Long): Result<Company> =
        remoteDataSource.getCompanyById(companyId).map { companyDto ->
            companyMapper.mapToDomain(companyDto)
        }

    override suspend fun register(companyToRegister: CompanyInput): Result<Company> {
        return try {
            val response = remoteDataSource.register(companyToRegister)
            response.mapCatching { companyDto ->
                companyMapper.mapToDomain(companyDto)
            }
        } catch (e: Exception) {
            Result.failure(Exception("Error registering the company", e))
        }
    }

    override suspend fun updateCompany(
        company: CompanyUpdateDto,
        profileImage: File?
    ): Result<Company> =
        remoteDataSource.updateCompany(company, profileImage).map { companyDto ->
            companyMapper.mapToDomain(companyDto)
        }
}