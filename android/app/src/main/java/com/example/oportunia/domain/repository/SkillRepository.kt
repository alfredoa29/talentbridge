package com.example.oportunia.domain.repository

import com.example.oportunia.domain.model.Skill

public interface SkillRepository {

    suspend fun  getAllSkills(): Result<List<Skill>>

}
