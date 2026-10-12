package com.example.oportunia.data.repository

import com.example.oportunia.data.mapper.SkillMapper
import com.example.oportunia.data.remote.SkillRemoteDataSource
import com.example.oportunia.domain.model.Skill
import com.example.oportunia.domain.repository.SkillRepository
import java.net.UnknownHostException
import javax.inject.Inject

class SkillRepositoryImpl @Inject constructor(
    private val remoteDataSource: SkillRemoteDataSource,
    private val skillMapper: SkillMapper
): SkillRepository{
    override suspend fun getAllSkills(): Result<List<Skill>> {

        return try {
            remoteDataSource.getAllSkills().map { skillsDtos ->
                skillMapper.mapToDomainList(skillsDtos)
            }
        } catch (e: UnknownHostException) {
            Result.failure(Exception("Network error: Cannot connect to server. Please check your internet connection."))
        } catch (e: Exception) {
            Result.failure(Exception("Error fetching countries: ${e.message}"))
        }
    }

}