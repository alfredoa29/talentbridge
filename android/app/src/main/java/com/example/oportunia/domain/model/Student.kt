package com.example.oportunia.domain.model

data class Student(
    override val id: Long,
    override val email: String,
    override val password: String,
    override val enabled: Boolean,
    override val tokenExpired: Boolean,
    val fullName: String,
    val university: String,
    val career: String,
    val username: String,
    val skillList: List<Skill>,  // Lista de habilidades/técnicas
    val areaOfInterestList: List<AreaOfInterest>,
    val profilePicUrl: String,
    val country: Country,
    val gender: Gender
) : User(id, email, password, enabled, tokenExpired){

}