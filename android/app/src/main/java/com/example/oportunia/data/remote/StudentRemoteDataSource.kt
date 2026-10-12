package com.example.oportunia.data.remote
import com.example.oportunia.data.remote.dto.InternshipDto
import com.example.oportunia.data.remote.dto.StudentDto
import com.example.oportunia.data.remote.api.StudentService
import com.example.oportunia.data.remote.dto.StudentInput
import com.example.oportunia.data.remote.dto.StudentUpdateDto
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject



class StudentRemoteDataSource @Inject constructor(
    private val studentService: StudentService,

) {

    // Fetches all students
    suspend fun getAllStudents(): Result<List<StudentDto>> = safeApiCall {
        studentService.getAllStudents()
    }

    // Fetches a student by their ID
    suspend fun getStudentById(id: Long): Result<StudentDto> = safeApiCall {
        studentService.getStudentById(id)
    }

    suspend fun register(studentInputDto: StudentInput): Result<StudentDto> = safeApiCall {
        studentService.registerStudent(studentInputDto)
    }

    suspend fun getInternshipsByStudent(studentId: Long): Result<List<InternshipDto>> = safeApiCall {
        studentService.getInternshipsByStudent(studentId)
    }

    suspend fun updateStudent(student: StudentUpdateDto, profileImage: File? = null): Result<StudentDto> = safeApiCall {
        //esto es para convertir en multipart
        val gson = Gson()
        val studentJson = gson.toJson(student)
        val studentBody = studentJson.toRequestBody("application/json".toMediaTypeOrNull())

        val imagePart = profileImage?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", it.name, requestFile)
        }

        studentService.updateStudent(studentBody, imagePart)
    }

    // Generic method to safely handle API calls
    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> = try {
        val response = apiCall()
        if (response.isSuccessful) {
            response.body()?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Response body was null"))
        } else {
            val errorBody = response.errorBody()?.string()
            Result.failure(Exception("API error ${response.code()}: $errorBody"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }



}
