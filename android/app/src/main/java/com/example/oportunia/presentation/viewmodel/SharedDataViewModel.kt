package com.example.oportunia.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oportunia.domain.model.AreaOfInterest
import com.example.oportunia.domain.model.Country
import com.example.oportunia.domain.model.Gender
import com.example.oportunia.domain.model.Sector
import com.example.oportunia.domain.model.Skill
import com.example.oportunia.domain.repository.AreaOfInterestRepository
import com.example.oportunia.domain.repository.CountryRepository
import com.example.oportunia.domain.repository.GenderRepository
import com.example.oportunia.domain.repository.InternshipRepository
import com.example.oportunia.domain.repository.SectorRepository
import com.example.oportunia.domain.repository.SkillRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed class SharedCategoryState {
    object Loading : CategoryState() // Cuando la aplicación está en proceso de creación.
    object Success : CategoryState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : CategoryState() // Cuando ocurre un error.
}

sealed class CountryState {
    object Loading : CountryState() // Cuando la aplicación está en proceso de creación.
    object Success : CountryState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : CountryState() // Cuando ocurre un error.
}

sealed class GenderState {
    object Loading : GenderState() // Cuando la aplicación está en proceso de creación.
    object Success : GenderState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : GenderState() // Cuando ocurre un error.
}

sealed class SkillState {
    object Loading : SkillState() // Cuando la aplicación está en proceso de creación.
    object Success : SkillState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : SkillState() // Cuando ocurre un error.
}

sealed class SectorState {
    object Loading : SectorState() // Cuando la aplicación está en proceso de creación.
    object Success : SectorState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : SectorState() // Cuando ocurre un error.
}

@HiltViewModel
class SharedDataViewModel @Inject constructor(
    private val internshipRepository: InternshipRepository,
    private val areaOfInternshipRepository: AreaOfInterestRepository,
    private val countryRepository: CountryRepository,
    private val genderRepository: GenderRepository,
    private val skillRepository: SkillRepository,
    private val SectorRepository: SectorRepository
): ViewModel() {

    private val _categoryList = MutableStateFlow<List<AreaOfInterest>>(emptyList())

    val categoryList = _categoryList.asStateFlow()

    private val _countryList = MutableStateFlow<List<Country>>(emptyList())
    val countryList = _countryList.asStateFlow()

    private val _genderList = MutableStateFlow<List<Gender>>(emptyList())
    val genderList = _genderList.asStateFlow()

    private val _skillList = MutableStateFlow<List<Skill>>(emptyList())
    val skillList = _skillList.asStateFlow()

    private val _sectorList = MutableStateFlow<List<Sector>>(emptyList())
    val sectorList = _sectorList.asStateFlow()

    private val _currentUser  = mutableStateOf<Long?>(null)
    val currentUser: State<Long?> = _currentUser


    // Método para actualizar el studentId
    fun setuser(id: Long) {
        _currentUser.value = id
    }

    fun getAllCategories(){ //areaOfInternshipRepository
        viewModelScope.launch {
            _categoryList.value = emptyList()
            areaOfInternshipRepository.getAllAreaOfInteres()
                .onSuccess { categories ->
                    _categoryList.value = categories

                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching categories: ${exception.message}")
                }
        }
    }

    fun getAllCountries(){
        viewModelScope.launch {
            _countryList.value = emptyList()
            countryRepository.getAllCountries()
                .onSuccess { countries ->
                    _countryList.value = countries
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching categories: ${exception.message}")
                }
        }
    }

    fun getAllGenders(){
        viewModelScope.launch {
            _genderList.value = emptyList()
            genderRepository.getAllGenders()
                .onSuccess { genders ->
                    _genderList.value = genders
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching categories: ${exception.message}")
                }
        }
    }

    fun getAllSkills(){
        viewModelScope.launch {
            _skillList.value = emptyList()
            skillRepository.getAllSkills()
                .onSuccess { skills ->
                    _skillList.value = skills
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching categories: ${exception.message}")
                }
        }
    }

    fun getAllSectors(){
        viewModelScope.launch {
            _sectorList.value = emptyList()
            SectorRepository.getAllSectors()
                .onSuccess { sectors ->
                    _sectorList.value = sectors
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching categories: ${exception.message}")
                }
        }
    }

}