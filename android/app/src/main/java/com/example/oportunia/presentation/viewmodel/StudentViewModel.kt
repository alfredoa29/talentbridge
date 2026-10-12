package com.example.oportunia.viewmodel

import android.content.ContentResolver
import android.util.Log
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


import androidx.lifecycle.viewModelScope

import com.example.oportunia.domain.model.Application

import com.example.oportunia.domain.model.Internship
import com.example.oportunia.domain.model.Interview
import com.example.oportunia.domain.model.Student
import com.example.oportunia.domain.repository.ApplicationRepository
import com.example.oportunia.domain.repository.InterviewRepository
import com.example.oportunia.domain.repository.StudentRepository
import com.example.oportunia.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.example.oportunia.domain.model.AnalizeCurriculumAI
import com.example.oportunia.domain.repository.CVRepository
import androidx.compose.runtime.State
import com.example.oportunia.data.remote.dto.StudentUpdateDto
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.File


/**
 * Sealed class representing the state for fetching internships of a student.
 */
sealed class StudentInternshipState {
    data object Initial : StudentInternshipState()
    data object Loading : StudentInternshipState()
    data class Success(val internships: List<Internship>) : StudentInternshipState()
    data object Empty : StudentInternshipState()
    data class Error(val message: String) : StudentInternshipState()
}


sealed class InterviewState {
    data object Initial : InterviewState()
    data object Loading : InterviewState()
    data class Success(val interview: Interview) : InterviewState() // C
    data object Empty : InterviewState()
    data class Error(val message: String) : InterviewState()
}

sealed class ApplicationState {
    data object Initial : ApplicationState()
    data object Loading : ApplicationState()
    data class Success(val applications: List<Application>) : ApplicationState()
    data object Empty : ApplicationState()
    data class Error(val message: String) : ApplicationState()
}

sealed class CVUploadState {
    object Idle : CVUploadState()
    object Loading : CVUploadState()
    data class Success(val data: AnalizeCurriculumAI) : CVUploadState()
    data class Error(val message: String) : CVUploadState()
}


sealed class FileSelectionState {
    object Idle : FileSelectionState()
    object FileSelected : FileSelectionState()
}

/**
 * Sealed class representing the various states of a student operation.
 * Used to track the loading, success, error, and empty states for student-related operations.
 */
sealed class StudentState {
    /**
     * Initial state before any operation has started
     */
    data object Initial : StudentState()

    /**
     * Indicates an operation is in progress
     */
    data object Loading : StudentState()

    /**
     * Contains successfully retrieved task data
     * @property task The retrieved task
     */
    data class Success(val task: Student) : StudentState()

    /**
     * Indicates no task data is available
     */
    data object Empty : StudentState()

    /**
     * Represents an error condition during a task operation
     * @property message The error message describing what went wrong
     */
    data class Error(val message: String) : StudentState()
}

// Define los eventos que la UI puede manejar
sealed class UiEvent {
    data class ShowSnackbar(val message: String): UiEvent()
    object NavigateBack: UiEvent()
    // puedes agregar más eventos aquí
}


@HiltViewModel
class StudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val interviewRepository: InterviewRepository,
    private val userRepository: UserRepository, // Obtener el usuario actual para cargar datos en perfil

    private val applicationRepository: ApplicationRepository,

    private val cvRepository: CVRepository

) : ViewModel() {

    // Student list state
    private val _studentList = MutableStateFlow<List<Student>>(emptyList())
    val studentList = _studentList.asStateFlow()

    private  val _student = MutableStateFlow<StudentState>(StudentState.Initial)
    val student = _student.asStateFlow()

    // Selected student state
    private val _selectedStudent = MutableStateFlow<Student?>(null)
    val selectedStudent = _selectedStudent.asStateFlow()

    // Internships list for a specific student
    private val _studentInternships = MutableStateFlow<List<Internship>>(emptyList())
    val studentInternships = _studentInternships.asStateFlow()

    private val _internship = MutableStateFlow<StudentInternshipState>(StudentInternshipState.Initial)

    /**
     * Flow representing the current state of task detail operations
     * (loading, success, error, etc.)
     */
    val internship  = _internship.asStateFlow()




    // Pasantía seleccionada
    private val _selectedInternship = MutableStateFlow<Internship?>(null)

    val selectedInternship = _selectedInternship.asStateFlow()

    // Estado de carga de pasantías
    private val _studentInternshipState = MutableStateFlow<StudentInternshipState>(StudentInternshipState.Initial)
    val studentInternshipState = _studentInternshipState.asStateFlow()


    // Estado de carga de la entrevista
    private val _interviewState = MutableStateFlow<InterviewState>(InterviewState.Initial)
    val interviewState = _interviewState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    fun setLoadingState(isLoading: Boolean) {
        _isLoading.value = isLoading
    }

    // atributos de application

    // Lista de aplicaciones
    private val _applicationList = MutableStateFlow<List<Application>>(emptyList())
    val applicationList = _applicationList.asStateFlow()

    // Task detail state
    private val _application= MutableStateFlow<ApplicationState>(ApplicationState.Initial)

    /**
     * Flow representing the current state of task detail operations
     * (loading, success, error, etc.)
     */
    val application = _application.asStateFlow()

    // Estado de las aplicaciones
    private val _selectedApplication = MutableStateFlow<Application?>(null)
    val selectecApplication = _selectedApplication.asStateFlow()






    val selectedApplication = _selectedApplication.asStateFlow()



    // Estado del archivo seleccionado
    private val _selectedFileUri = MutableStateFlow<Uri?>(null)
    val selectedFileUri: StateFlow<Uri?> = _selectedFileUri


    // Estado de la subida del CV
    private val _uploadResult = MutableStateFlow<CVUploadState>(CVUploadState.Idle)
    val uploadResult: StateFlow<CVUploadState> = _uploadResult



    private val _currentAnalysis = mutableStateOf<AnalizeCurriculumAI?>(null)
    val currentAnalysis: State<AnalizeCurriculumAI?> = _currentAnalysis

    private val _fileSelectionState = MutableStateFlow<FileSelectionState>(FileSelectionState.Idle)
    val fileSelectionState: StateFlow<FileSelectionState> = _fileSelectionState


    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun setAnalysis(analysis: AnalizeCurriculumAI) {
        _currentAnalysis.value = analysis
    }



    // Método para seleccionar una pasantía por ID
    fun selectInternshipById(internshipId: Long) {
        _selectedApplication.value = _applicationList.value.find { it.id == internshipId }
    }

    /**
     * Fetches all students from the repository and updates [studentList].
     * Any errors encountered during the operation are logged.
     */
    fun findAllStudents() {
        viewModelScope.launch {
            _studentList.value = emptyList() // Clear before loading

            studentRepository.findAllStudents()
                .onSuccess { students ->
                    Log.d("StudentViewModel", "Students received: $students")
                    _studentList.value = students
                }
                .onFailure { exception ->
                    Log.e("StudentViewModel", "Error fetching students: ${exception.message}")
                }
        }
    }

    /**
     * Fetches a specific student by ID and updates [selectedStudent].
     *
     * @param studentId The unique identifier of the student to retrieve.
     */
    //revisar esto porque le paso un internship id en company

    fun selectStudentById(studentId: Long) {
        viewModelScope.launch {
          //  _student.value = StudentState.Loading
            val result = studentRepository.findStudentById(studentId)
            result
                .onSuccess { student ->

                    _selectedStudent.value = student
                    Log.d("StudentViewModel", "Students received: $student")
                }
                .onFailure { exception ->
                    _student.value = StudentState.Error(exception.message ?: "Unknown error")
                    Log.e("StudentViewModel", "Error al obtener estudiante", exception)
                    _selectedStudent.value = null
                }
        }
    }



    /**
     * Fetches internships for a specific student by studentId and updates [studentInternships] and [studentInternshipState].
     */
    fun generateInterview(internshipId: Long) {
        viewModelScope.launch {
            _interviewState.value = InterviewState.Loading  // Estado de carga

            try {
                // Delegar la lógica al repositorio para generar la entrevista
                val result = interviewRepository.generateInterview(internshipId)

                // Manejar el resultado usando el onSuccess y onFailure
                result.onSuccess { interview ->
                    // Si la operación fue exitosa, actualizamos el estado con el objeto Interview
                    _interviewState.value = InterviewState.Success(interview)
                }.onFailure { exception ->
                    // En caso de error, actualizamos el estado de error
                    _interviewState.value = InterviewState.Error(exception.message ?: "Error desconocido")
                }
            } catch (exception: Exception) {
                // Si ocurre un error fuera del repositorio, también se actualiza el estado
                _interviewState.value = InterviewState.Error(exception.message ?: "Error desconocido")
            }
        }
    }

    fun getInternshipsByStudent(studentId: Long) {
        viewModelScope.launch {
            _application.value = ApplicationState.Loading
            _applicationList.value = emptyList()


            applicationRepository.getApplicationsByStudent(studentId)
                .onSuccess { applications ->
                    _applicationList.value = applications
                    _application.value = ApplicationState.Success(applications)
                }

                .onFailure { exception ->
                    Log.e("StudentViewModel", "Error fetching Applicationinternships", exception)
                    _studentInternshipState.value = StudentInternshipState.Error(exception.message ?: exception.toString())
                    Log.e("StudentViewModel", "Error fetching Applicationinternships: ${exception.message}")
                }
        }
    }



    fun setSelectedFileUri(uri: Uri?) {
        _selectedFileUri.value = uri
        if (uri != null) {
            _fileSelectionState.value = FileSelectionState.FileSelected
        } else {
            _fileSelectionState.value = FileSelectionState.Idle
        }
    }
    fun resetFileSelectionState() {
        _fileSelectionState.value = FileSelectionState.Idle
    }

    fun uploadCV(fileUri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            _uploadResult.value = CVUploadState.Loading

            val result = cvRepository.uploadCV(fileUri, contentResolver)

            _uploadResult.value = result.fold(
                onSuccess = {

                    setAnalysis(it)
                    CVUploadState.Success(it)
                },
                onFailure = {
                    CVUploadState.Error(it.message ?: "Unknown error")
                }
            )
        }
    }

    fun updateStudent(studenDto: StudentUpdateDto, imageUri: File?){
        viewModelScope.launch {
            _student.value = StudentState.Initial

            studentRepository.updateStudent(studenDto, imageUri)
                .onSuccess { student ->
                    _student.value = StudentState.Success(student)
                    _selectedStudent.value = student
                    _uiEvent.emit(UiEvent.ShowSnackbar("Actualización exitosa"))

                }
                .onFailure { exception ->
                    _student.value = StudentState.Error(exception.message ?: "Unknown error")
                    Log.e("StudentViewMOdel", "Error updating student by ID: ${exception.message}")
                    _uiEvent.emit(UiEvent.ShowSnackbar("Error: ${exception.message ?: "Unknown error"}"))
                }
        }

    }


    fun resetUploadState() {
        _uploadResult.value = CVUploadState.Idle
    }

    fun clearSelectedFileUri() {
        _selectedFileUri.value = null
    }

    fun clearStudentState() {
        _student.value = StudentState.Initial
    }

    suspend fun getStudentIdByUsername(username: String): Long {
        val studentResult = userRepository.findUserByUsername(username)

        return studentResult.getOrThrow().id
    }





}
