package com.example.oportunia.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oportunia.data.remote.dto.CompanyInput
import com.example.oportunia.data.remote.dto.StudentInput
import com.example.oportunia.domain.repository.AuthRepository
import com.example.oportunia.domain.repository.CompanyRepository
import com.example.oportunia.domain.repository.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartUpViewModel @Inject constructor() : ViewModel() {

    private val _selectedTab = MutableStateFlow(0)

    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>   (LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()



    private val _userRole = MutableStateFlow<String?>(null)
    val userRole: StateFlow<String?> = _userRole.asStateFlow()


    fun onEmailChanged(email: String) {
        _email.value = email
    }

    fun onPasswordChanged(password: String) {
        _password.value = password
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading


    init {
        checkAuthenticationStatus()
    }

    /**
     * Checks if user is already authenticated when ViewModel is initialized
     */
    private fun checkAuthenticationStatus() {
        viewModelScope.launch {
            authRepository.isAuthenticated()
                .onSuccess { authenticated ->
                    _isLoggedIn.value = authenticated
                    Log.d("LoginViewModel", "Authentication status checked: $authenticated")
                }
                .onFailure {
                    Log.e("LoginViewModel", "Error checking authentication status: ${it.message}")
                }
        }
    }

    fun login(email: String, password: String) {

        viewModelScope.launch {
            _isLoading.value = true
            _uiState.value = LoginUiState.Loading

            // Validate inputs before attempting network call
            if (!isValidEmail(email)) {
                _uiState.value = LoginUiState.Error("Invalid email format")
                _isLoading.value = false
                return@launch
            }

            authRepository.login(email, password)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                    _isLoggedIn.value = true
                    Log.d("LoginViewModel", "Login successful for user: $email")
                }
                .onFailure { exception ->
                    _uiState.value =
                        LoginUiState.Error(exception.message ?: "Authentication failed")
                    Log.e(
                        "LoginViewModel",
                        "Login failed for user: $email, error: ${exception.message}"
                    )
                }

            _isLoading.value = false
        }
    }

    /**
     * Validates if the provided string is a valid email address.
     *
     * @param email The email address to validate
     * @return True if the email is valid, false otherwise
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Logs the user out using the auth repository.
     * Clears authentication data and resets UI state.
     */
    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true

            authRepository.logout()
                .onSuccess {
                    _isLoggedIn.value = false
                    _uiState.value = LoginUiState.Initial
                    Log.d("LoginViewModel", "User logged out successfully")
                }
                .onFailure { exception ->
                    Log.e("LoginViewModel", "Logout failed: ${exception.message}")
                    // Even if remote logout fails, we should reset the UI state
                    _isLoggedIn.value = false
                    _uiState.value = LoginUiState.Initial
                }

            _isLoading.value = false
        }
    }

    fun resetErrorState() {
        _uiState.value = LoginUiState.Initial
    }

    sealed class LoginUiState {
        data object Initial : LoginUiState()
        data object Loading : LoginUiState()
        data object Success : LoginUiState()
        data class Error(val message: String) : LoginUiState()
    }


    fun autoLogin() {
        viewModelScope.launch {
            val usernameResult = authRepository.getUsername()
            val tokenResult = authRepository.getAuthToken()

            val username = usernameResult.getOrNull()
            val token = tokenResult.getOrNull()

            if (!username.isNullOrEmpty() && !token.isNullOrEmpty()) {
                Log.d("LoginViewModel", "AutoLogin: Success with $username")
                _uiState.value = LoginUiState.Success
                _isLoggedIn.value = true
            } else {
                Log.d("LoginViewModel", "AutoLogin failed: username=$username, token=$token")
                logout()
            }
        }
    }
}

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val studentRepository: StudentRepository,
    private val companyRepository: CompanyRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Initial)
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _verifyPassword = MutableStateFlow("")
    val verifyPassword: StateFlow<String> = _verifyPassword.asStateFlow()

    private val _selectedRole = MutableStateFlow("")
    val selectedRole: StateFlow<String> = _selectedRole.asStateFlow()

    fun onEmailChanged(email: String) { _email.value = email }
    fun onPasswordChanged(password: String) { _password.value = password }
    fun onVerifyPasswordChanged(verifyPassword: String) { _verifyPassword.value = verifyPassword }
    fun onSelectedRoleChanged(role: String) { _selectedRole.value = role }

    fun register() {
        if (_password.value.trim() != _verifyPassword.value.trim()) {
            _uiState.value = RegisterUiState.Error("Las contraseñas no coinciden")
            return
        }

        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading

            val email = _email.value
            val password = _password.value

            val inputS = StudentInput(
                email = email,
                password = password,
            )

            val inputC = CompanyInput(
                email = email,
                password = password,
            )

            val registrationResult = when (_selectedRole.value) {
                "Estudiante" -> studentRepository.register(inputS)
                "Empresa" -> companyRepository.register(inputC)
                else -> Result.failure(Exception("Rol inválido"))
            }

            registrationResult.fold(
                onSuccess = {
                    authRepository.login(email, password).fold(
                        onSuccess = {
                            _uiState.value = RegisterUiState.Success
                        },
                        onFailure = { loginError ->
                            _uiState.value = RegisterUiState.Error(
                                loginError.message ?: "Error al iniciar sesión"
                            )
                        }
                    )
                },
                onFailure = { regError ->
                    _uiState.value = RegisterUiState.Error(regError.message ?: "Error al registrarse")
                }
            )
        }
    }

    fun resetErrorState() {
        _uiState.value = RegisterUiState.Initial
    }

    sealed class RegisterUiState {
        data object Initial : RegisterUiState()
        data object Loading : RegisterUiState()
        data object Success : RegisterUiState()
        data class Error(val message: String) : RegisterUiState()
    }
}