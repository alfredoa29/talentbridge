package com.example.oportunia.domain.repository

import com.example.oportunia.data.remote.dto.CompanyInput
import com.example.oportunia.data.remote.dto.CompanyUpdateDto
import com.example.oportunia.domain.model.Company
import java.io.File

interface CompanyRepository {
    suspend fun findCompanyById(companyId: Long): Result<Company>
    suspend fun register(companyToRegister: CompanyInput): Result<Company>
    suspend fun updateCompany(company: CompanyUpdateDto, profileImage: File? = null): Result<Company>
}