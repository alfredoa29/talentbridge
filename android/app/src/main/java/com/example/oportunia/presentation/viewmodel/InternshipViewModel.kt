package com.example.oportunia.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.domain.model.Application
import com.example.oportunia.domain.model.CreateApplication
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.domain.repository.ApplicationRepository
import com.example.oportunia.domain.repository.InternshipRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Sealed class representing the various states of an internship operation.
 * Used to track the loading, success, error, and empty states for internship operations.
 */
sealed class InternshipState {
    /**
     * Initial state before any operation has started.
     */
    data object Initial : InternshipState()

    /**
     * Indicates an operation is in progress.
     */
    data object Loading : InternshipState()

    /**
     * Contains successfully retrieved internship data.
     * @property internship The retrieved internship.
     */
    data class Success(val internship: Internship) : InternshipState()

    /**
     * Indicates no internship data is available.
     */
    data object Empty : InternshipState()

    /**
     * Represents an error condition during an internship operation.
     * @property message The error message describing what went wrong.
     */
    data class Error(val message: String) : InternshipState()
}

sealed class ApplicationState {
    object Loading : ApplicationState() // Cuando la aplicación está en proceso de creación.
    data class Success(val application: Application) : ApplicationState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : ApplicationState() // Cuando ocurre un error.
}


/**
 * ViewModel responsible for managing internship-related UI state and business logic.
 *
 * This ViewModel handles:
 * - Retrieving internship lists and individual internships.
 * - Managing loading states during repository operations.
 * - Exposing internship data via StateFlows for reactive UI updates.
 * - Error handling for internship operations.
 *
 * @property repository Repository interface for internship operations.
 */
@HiltViewModel
class InternshipViewModel  @Inject constructor(
    private val repository: InternshipRepository,
    private val applicationRepository: ApplicationRepository
) : ViewModel() {

    // Internship list state
    private val _internshipList = MutableStateFlow<List<Internship>>(emptyList())
    /**
     * Flow representing the list of all internships.
     */
    val internshipList = _internshipList.asStateFlow()

    // Task detail state
    private val _internship = MutableStateFlow<InternshipState>(InternshipState.Initial)

    /**
     * Flow representing the current state of task detail operations
     * (loading, success, error, etc.)
     */
    val internship = _internship.asStateFlow()


    // Selected internship state
    private val _selectedInternship = MutableStateFlow<Internship?>(null)
    /**
     * Flow representing the currently selected internship, if any.
     */
    val selectedInternship = _selectedInternship.asStateFlow()




    //valores para el circulo de carga 
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    /* para manejar los estados de application, si se envio la aplicacion a la pasantia o no*/
    private val _applicationState = MutableStateFlow<ApplicationState>(ApplicationState.Loading)
    val applicationState = _applicationState.asStateFlow()


    fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private var internshipJob: Job? = null

    /**
     * Fetches all internships from the repository and updates [internshipList].
     * Any errors encountered during the operation are logged.
     */
    fun findAllInternships() {
        viewModelScope.launch {
            _internship.value = InternshipState.Loading
            repository.findAllInternships()
                .onSuccess { internships ->
                    Log.d("InternshipViewModel", "Internships received: $internships")
                    _internshipList.value = internships
                    _internship.value = if (internships.isNotEmpty()) {
                        InternshipState.Success(internships.first()) // O algún Success genérico
                    } else {
                        InternshipState.Empty
                    }
                }
                .onFailure { exception ->
                    Log.e("InternshipViewModel", "Error fetching internships: ${exception.message}")
                    _internship.value = InternshipState.Error(exception.message ?: "Error desconocido")
                }
        }
    }


    fun findAllInternshipsByCompanyId(companyId: Long){
        viewModelScope.launch {
            _internship.value = InternshipState.Loading
            repository.findAllInternshipsByCompanyId(companyId)
                .onSuccess { internships ->
                    Log.d("InternshipViewModel", "Internships by company id received: $internships")
                    _internshipList.value = internships
                    _internship.value = if (internships.isNotEmpty()) {
                        InternshipState.Success(internships.first()) // O algún Success genérico
                    } else {
                        InternshipState.Empty
                    }
                }
                .onFailure { exception ->
                    Log.e("InternshipViewModel", "Error fetching internships: ${exception.message}")
                    _internship.value = InternshipState.Error(exception.message ?: "Error desconocido")
                }
        }
    }

    fun findAllInternshipsByStudentId(studentId: Long){
        viewModelScope.launch {
            _internship.value = InternshipState.Loading
            repository.findAllInternshipsByStudentId(studentId)
                .onSuccess { internships ->
                    Log.d("InternshipViewModel", "Internships received: $internships")
                    _internshipList.value = internships
                    _internship.value = if (internships.isNotEmpty()) {
                        InternshipState.Success(internships.first()) // O algún Success genérico
                    } else {
                        InternshipState.Empty
                    }
                }
                .onFailure { exception ->
                    Log.e("InternshipViewModel", "Error fetching internships: ${exception.message}")
                    _internship.value = InternshipState.Error(exception.message ?: "Error desconocido")
                }
        }
    }

    /**
     * Fetches a specific internship by ID and updates [selectedInternship].
     *
     * @param internshipId The unique identifier of the internship to retrieve.
     */
    fun selectInternshipById(internshipId: Long) {
        internshipJob?.cancel()
        internshipJob = viewModelScope.launch {
            _internship.value = InternshipState.Loading
            repository.findTaskById(internshipId)
                .onSuccess { internship ->
                    _internship.value = InternshipState.Success(internship)
                    _selectedInternship.value = internship
                }
                .onFailure { exception ->
                    _internship.value = InternshipState.Error(exception.message ?: "Unknown error")
                    Log.e("Internshipviewmodel", "Error fetching internship by ID: ${exception.message}")
                }
        }
    }




    fun applyToInternship(internshipId: Long, studentId: Long) {
        viewModelScope.launch {
            _applicationState.value = ApplicationState.Loading // Cambiar el estado a "Loading"
            val application = CreateApplication(
                internshipId = internshipId,
                studentId = studentId,
                statusId = 1 // Estado "PENDING"
            )
            val result = applicationRepository.createApplication(application)

            _applicationState.value = if (result.isSuccess) {
                ApplicationState.Success(result.getOrNull()!!) // Si es exitoso, actualizar con la aplicación
            } else {

                val exception = result.exceptionOrNull()
                Log.e("ApplicationError", "Error al aplicar", exception)

                ApplicationState.Error(result.exceptionOrNull()?.message ?: "Error desconocido") // Manejar el error
            }
        }
    }

    fun clearSelectedInternship() {
        _internship.value = InternshipState.Empty // o null según tu lógica
        _selectedInternship.value = null
    }



}
