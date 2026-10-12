package com.example.oportunia.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oportunia.data.remote.dto.AreaOfInterestDto
import com.example.oportunia.data.remote.dto.CompanyUpdateDto
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.data.remote.dto.StudentUpdateDto
import com.example.oportunia.domain.model.ApplicationStatusChange
import com.example.oportunia.domain.model.Company
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.domain.model.Student
import com.example.oportunia.domain.model.StudentInternship
import com.example.oportunia.domain.repository.ApplicationRepository
import com.example.oportunia.domain.repository.CompanyRepository
import com.example.oportunia.domain.repository.InternshipRepository
import com.example.oportunia.domain.repository.UserRepository
import com.example.oportunia.viewmodel.StudentState
import com.example.oportunia.viewmodel.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Sealed class representing the various states of login operations.
 */
sealed class StudentInternshipState {
    /**
     * Initial state before any operation has started
     */
    data object Initial: StudentInternshipState()
    /**
     * Indicates an operation is in progress
     */
    data object Loading: StudentInternshipState()

    /**
     * Contains successfully retrieved task data
     * @property studentInternship The retrieved studentInternship
     */
    data class Success(val studentInternship: StudentInternship): StudentInternshipState()

    /**
     * Indicates no studentInternship data is available
     */
    data object Empty: StudentInternshipState()
    /**
     * Represents an error condition during a task operation
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : StudentInternshipState()


}


sealed class ApplicationCompanyState{
    /**
     * Initial state before any operation has started
     */
    data object Initial: ApplicationCompanyState()
    /**
     * Indicates an operation is in progress
     */
    data object Loading: ApplicationCompanyState()

    /**
     * Contains successfully retrieved task data
     * @property application The retrieved studentInternship
     */
    data class Success(val application: com.example.oportunia.domain.model.Application): ApplicationCompanyState()

    /**
     * Indicates no studentInternship data is available
     */
    data object Empty: ApplicationCompanyState()
    /**
     * Represents an error condition during a task operation
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : ApplicationCompanyState()


}

sealed class UpdateApplicationState {
    object Loading : UpdateApplicationState() // Cuando la aplicación está en proceso de creación.
    object Success : UpdateApplicationState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : UpdateApplicationState() // Cuando ocurre un error.
}

/**
 * Sealed class representing the various states of Internship operations.
 */
sealed class CompanyInternshipState {
    /**
     * Initial state before any operation has started
     */
    data object Initial: CompanyInternshipState()
    /**
     * Indicates an operation is in progress
     */
    data object Loading: CompanyInternshipState()

    /**
     * Contains successfully retrieved task data
     * @property Internship The retrieved studentInternship
     */
    data class Success(val internship: Internship): CompanyInternshipState()

    /**
     * Indicates no studentInternship data is available
     */
    data object Empty: CompanyInternshipState()
    /**
     * Represents an error condition during a task operation
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : CompanyInternshipState()


}

sealed class CategoryState {
    object Loading : CategoryState() // Cuando la aplicación está en proceso de creación.
    object Success : CategoryState() // Resultado exitoso con los datos de la aplicación.
    data class Error(val errorMessage: String) : CategoryState() // Cuando ocurre un error.
}

sealed class CompanyState {
    object Initial : CompanyState() // Cuando la aplicación está en proceso de creación.
    object Loading : CompanyState() // Resultado exitoso con los datos de la aplicación.
    data class Success(val company: Company) : CompanyState() // Resultado exitoso con los datos de la aplicación.
    data class Empty(val message: String) : CompanyState() // Cuando no hay datos disponibles.
    data class Error(val errorMessage: String) : CompanyState() // Cuando ocurre un error.
}

@HiltViewModel
class CompanyViewModel  @Inject constructor(
    private val companyRepository: CompanyRepository,
    private val applicationRepository: ApplicationRepository,
    private val internshipRepository: InternshipRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    //studentInternship list state
    private val _studentInternshipList = MutableStateFlow<List<StudentInternship>>(emptyList())

    /**
     * Flow of the list of all studentInternship. Empty list means no studentInternship or loading.
     */
    val studentInternshipList = _studentInternshipList.asStateFlow()

    // studentInternship detail state
    private val _studentInternship = MutableStateFlow<StudentInternshipState>(StudentInternshipState.Initial)

    /**
     * Flow representing the current state of studentInternship detail operations
     * (loading, success, error, etc.)
     */
    val studentInternship = _studentInternship.asStateFlow()

    // Selected studentInternship state
    private val _seletectStudentInternship = MutableStateFlow<StudentInternship?>(null)

    /**
     * Flow of the currently selected studentInternship, if any

     */

    val selectedStudentInternship = _seletectStudentInternship.asStateFlow()

    //-------------------------------

    private val _applicationList = MutableStateFlow<List<Application>>(emptyList())

    /**
     * Flow of the list of all application. Empty list means no application or loading.
     */
    val applicationList = _applicationList.asStateFlow()

    // studentInternship detail state
    private val _application = MutableStateFlow<ApplicationCompanyState>(ApplicationCompanyState.Initial)

    /**
     * Flow representing the current state of application  detail operations
     * (loading, success, error, etc.)
     */
    val application = _application.asStateFlow()


    // Selected studentInternship state
    private val _selectedtApplication = MutableStateFlow<com.example.oportunia.domain.model.Application?>(null)

    /**
     * Flow of the currently selected studentInternship, if any

     */

    val selectedtApplication = _selectedtApplication.asStateFlow()
    //-------------------------------

    // studentInternship detail state
    private val _internship = MutableStateFlow<CompanyInternshipState>(CompanyInternshipState.Initial)

    /**
     * Flow representing the current state of studentInternship detail operations
     * (loading, success, error, etc.)
     */
    val internship = _internship.asStateFlow()

    private val _selectedInternship = MutableStateFlow<Internship?>(null) //revisar bien el tipo de Internship
    val selectedInternship = _selectedInternship.asStateFlow()
    //------------------------------------------
    //studentInternship list state
    private val _categoryList = MutableStateFlow<List<AreaOfInterestDto>>(emptyList())

    val categoryList = _categoryList.asStateFlow()

    private  val _company = MutableStateFlow<CompanyState>(CompanyState.Initial)
    val student = _company.asStateFlow()

    // Internship list state
    private val _internshipList = MutableStateFlow<List<Internship>>(emptyList())
    /**
     * Flow representing the list of all internships.
     */
    val internshipList = _internshipList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    private val _selectedCompany = MutableStateFlow<Company?>(null)
    val selectedCompany = _selectedCompany.asStateFlow()

    //para manejar  updateapplicationstate
    private val _updateApplicationState = MutableStateFlow<UpdateApplicationState>(UpdateApplicationState.Loading)
    val updateApplicationState = _updateApplicationState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>() //UiEvent es una sealedClass que esta en studentviewmodel
    val uiEvent = _uiEvent.asSharedFlow()

    fun selectCompanyById(companyId: Long) {
        viewModelScope.launch {
            val result = companyRepository.findCompanyById(companyId)
            result
                .onSuccess { company ->
                    _selectedCompany.value = company
                    Log.d("CompanyViewModel", "Company selected: $company")
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error al obtener la compañía", exception)
                    _selectedCompany.value = null
                }
        }
    }

    fun getStudentsAndInternshipsByCompany(companyId: Long){
        viewModelScope.launch {
            _studentInternshipList.value = emptyList()
            applicationRepository.getStudentsAndInternshipsByCompany(companyId)
                .onSuccess { studentInternships ->
                    _studentInternshipList.value = studentInternships
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching StudentsAndInternships: ${exception.message}")
                }
        }
    }

    fun getCategories(){
        viewModelScope.launch {
            _categoryList.value = emptyList()
            internshipRepository.getAllCategories()
                .onSuccess { categories ->
                    _categoryList.value = categories

                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching categories: ${exception.message}")
                }
        }
    }

    fun getApplicationById(applicationId: Long){
        viewModelScope.launch {
            _application.value = ApplicationCompanyState.Loading
            applicationRepository.getApplicationById(applicationId)

                .onSuccess { application ->
                    _application.value = ApplicationCompanyState.Success(application)
                    _selectedtApplication.value = application
                    Log.i("DEBUG_Application", "Application: ${application}")
                }
                .onFailure { exception ->
                    Log.e("CompanyViewModel", "Error fetching Application By Id: ${exception.message}")
                }
        }
    }

    fun changeApplicationStatus(applicationId: Long, newState: Int){
        viewModelScope.launch {
            _updateApplicationState.value = UpdateApplicationState.Loading
            val applicationStatusChange = ApplicationStatusChange(
                applicationId = applicationId,
                statusId = newState

            )
            applicationRepository.updateApplicationState(applicationStatusChange)
                .onSuccess {
                    _updateApplicationState.value = UpdateApplicationState.Success
                }
                .onFailure { exception ->
                    _updateApplicationState.value =
                        UpdateApplicationState.Error(exception.message?: "Error update Application State")
                    Log.e(
                        "CompanyViewModel",
                        "Error update Application state, error: ${exception.message}"
                    )
                }


        }

    }

    fun updateCompany(companyDto: CompanyUpdateDto, imageUri: File?){
        viewModelScope.launch {
            _company.value = CompanyState.Initial

            companyRepository.updateCompany(companyDto, imageUri)
                .onSuccess { company ->
                    _company.value = CompanyState.Success(company)
                    _selectedCompany.value = company
                    _uiEvent.emit(UiEvent.ShowSnackbar("Actualización exitosa"))

                }
                .onFailure { exception ->
                    _company.value = CompanyState.Error(exception.message ?: "Unknown error")
                    Log.e("StudentViewMOdel", "Error updating student by ID: ${exception.message}")
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${exception.message ?: "Unknown error"}"))
                }
        }

    }

     fun createInternship(internshipInputDto: InternshipInputDto){
         viewModelScope.launch {
             _internship.value = CompanyInternshipState.Loading

             internshipRepository.createInternship(internshipInputDto)
                 .onSuccess { internship ->
                     _internship.value = CompanyInternshipState.Success(internship)
                     _uiEvent.emit(UiEvent.ShowSnackbar("Pasantía registrada exitosamente"))
                 }
                 .onFailure { exception ->
                     _internship.value =
                         CompanyInternshipState.Error(exception.message?: "Error update Application State")
                     _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${exception.message ?: "Unknown error"}"))
                     Log.e(
                         "CompanyViewModel",
                         "Error update Application state, error: ${exception.message}"
                     )
                 }


         }

     }

    fun findAllInternshipsByCompanyId(companyId: Long){
        viewModelScope.launch {
            _internship.value = CompanyInternshipState.Loading
            internshipRepository.findAllInternshipsByCompanyId(companyId)
                .onSuccess { internships ->
                    Log.d("InternshipViewModel", "Internships by company id received: $internships")
                    _internshipList.value = internships

                   //
                }
                .onFailure { exception ->
                    Log.e("InternshipViewModel", "Error fetching internships: ${exception.message}")
                    _internship.value = CompanyInternshipState.Error(exception.message ?: "Error desconocido")
                }
        }
    }


    fun updateInternship(internshipInputDto: InternshipInputDto, companyId: Long) {
        viewModelScope.launch {
            _internship.value = CompanyInternshipState.Loading
            internshipRepository.updateInternship(internshipInputDto)
                .onSuccess { internship ->
                    _internship.value = CompanyInternshipState.Success(internship)
                    findAllInternshipsByCompanyId(companyId)
                    _uiEvent.emit(UiEvent.ShowSnackbar("Actualización exitosa"))
                }
                .onFailure { exception ->
                    _internship.value = CompanyInternshipState.Error(exception.message ?: "Unknown error")
                    Log.e("Internshipviewmodel", "Error update internship: ${exception.message}")
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error: Error al crear la pasantía"))
                }
        }
    }

    fun onShowSnackbarRequested(message: String) {
        viewModelScope.launch {
            _uiEvent.emit(UiEvent.ShowSnackbar(message))
        }
    }

    suspend fun getCompanyIdByUsername(username: String): Long {
        val studentResult = userRepository.findUserByUsername(username)

        return studentResult.getOrThrow().id
    }


}