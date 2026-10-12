package com.example.oportunia

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.navigation.NavGraph
import com.example.oportunia.navigation.NavGraphCompany
import com.example.oportunia.presentation.ui.components.BottoNavigationBarCompany
import com.example.oportunia.presentation.ui.components.BottonNavigationBarStudent

import com.example.oportunia.presentation.ui.screens.loginScreen.StartUpScreen
import com.example.oportunia.presentation.ui.theme.OportuniaTheme
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.RegisterViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.presentation.viewmodel.StartUpViewModel
import com.example.oportunia.viewmodel.StudentViewModel
import androidx.compose.material3.CircularProgressIndicator

import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private val startUpViewModel: StartUpViewModel by viewModels()
    private val registerViewModel: RegisterViewModel by viewModels()
    private val internshipViewModel: InternshipViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private val companyViewModel: CompanyViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewModel by viewModels()

    @Inject
    lateinit var authPreferences: AuthPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val loginUiState by loginViewModel.uiState.collectAsState()
            val isLoggedIn by loginViewModel.isLoggedIn.collectAsState()

            val registerUiState by registerViewModel.uiState.collectAsState()
            val email by registerViewModel.email.collectAsState()
            val password by registerViewModel.password.collectAsState()

            var userRole by remember { mutableStateOf<String?>(null) }
            var biometricVerified by remember { mutableStateOf(false) }
            var showBiometric by remember { mutableStateOf(false) }
            val context = LocalContext.current

            // Verificar si debe pedirse biometría
            LaunchedEffect(Unit) {
                val hasToken = !authPreferences.getAuthToken().isNullOrEmpty()
                val biometricEnabled = authPreferences.isBiometricEnabled()
                if (hasToken && biometricEnabled) {
                    showBiometric = true
                } else {
                    biometricVerified = true
                }
            }

            // Mostrar prompt biométrico solo una vez
            LaunchedEffect(showBiometric) {
                if (showBiometric) {
                    showBiometricPrompt(
                        onSuccess = {
                            biometricVerified = true
                            loginViewModel.autoLogin()
                        },
                        onError = {
                            biometricVerified = true
                        }
                    )
                }
            }

            LaunchedEffect(registerUiState) {
                when (val state = registerUiState) {
                    is RegisterViewModel.RegisterUiState.Success -> {
                        loginViewModel.login(email, password)
                        registerViewModel.resetErrorState()
                    }
                    is RegisterViewModel.RegisterUiState.Error -> {
                        Log.e("Register", "Failed: ${state.message}")
                    }
                    else -> {}
                }
            }

            // Lógica de carga de datos según login y rol
            LaunchedEffect(isLoggedIn) {
                if (isLoggedIn) {
                    userRole = authPreferences.getUserRole()
                    val username = authPreferences.getUsername()

                    when (userRole) {
                        "ROLE_STUDENT" -> {
                            username?.let {
                                val studentId = studentViewModel.getStudentIdByUsername(it)
                                sharedDataViewModel.setuser(studentId)
                                internshipViewModel.findAllInternships()
                            }
                        }
                        "ROLE_COMPANY" -> {
                            username?.let {
                                val companyId = companyViewModel.getCompanyIdByUsername(it)
                                sharedDataViewModel.setuser(companyId)
                            }
                        }
                        else -> {
                            loginViewModel.logout()
                        }
                    }
                }
            }

            OportuniaTheme {
                when {
                    !biometricVerified -> {
                        // Puedes mostrar una pantalla de carga mientras se verifica la biometría
                        LoadingScreen()
                    }

                    isLoggedIn && userRole != null -> {
                        Scaffold(
                            bottomBar = {
                                when (userRole) {
                                    "ROLE_STUDENT" -> BottonNavigationBarStudent(navController, internshipViewModel)
                                    "ROLE_COMPANY" -> BottoNavigationBarCompany(navController)
                                }
                            }
                        ) { innerPadding ->
                            when (userRole) {
                                "ROLE_STUDENT" -> NavGraph(
                                    navController = navController,
                                    internshipViewModel = internshipViewModel,
                                    studentViewModel = studentViewModel,
                                    sharedDataViewModel = sharedDataViewModel,
                                    loginViewModel = loginViewModel,
                                    modifier = Modifier.padding(innerPadding),
                                    authPreferences = authPreferences
                                )

                                "ROLE_COMPANY" -> NavGraphCompany(
                                    navController = navController,
                                    internshipViewModel = internshipViewModel,
                                    studentViewModel = studentViewModel,
                                    companyViewModel = companyViewModel,
                                    sharedDataViewModel = sharedDataViewModel,
                                    modifier = Modifier.padding(innerPadding),
                                    loginViewModel = loginViewModel
                                )
                            }
                        }
                    }

                    else -> {
                        StartUpScreen(
                            startUpViewModel = startUpViewModel,
                            loginViewModel = loginViewModel,
                            registerViewModel = registerViewModel,
                            onLoginSuccess = {},
                            onRegisterSuccess = {
                                loginViewModel.autoLogin()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun showBiometricPrompt(
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(this)
        val biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    Log.d("Biometric", "Authentication succeeded")
                    onSuccess()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    Log.d("Biometric", "Authentication error: $errString")
                    onError()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Log.d("Biometric", "Authentication failed")
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Iniciar sesión con biometría")
            .setSubtitle("Usa tu huella digital o rostro")
            .setNegativeButtonText("Usar contraseña")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Cargando...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
