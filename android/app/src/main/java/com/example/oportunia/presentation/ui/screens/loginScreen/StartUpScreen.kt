package com.example.oportunia.presentation.ui.screens.loginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.components.ActionButton
import com.example.oportunia.presentation.ui.components.CenteredDivider
import com.example.oportunia.presentation.ui.components.ErrorPopUp
import com.example.oportunia.presentation.ui.components.GeneralDropdown
import com.example.oportunia.presentation.ui.components.GeneralInput
import com.example.oportunia.presentation.ui.components.SocialLoginButton
import com.example.oportunia.presentation.ui.components.TabItem
import com.example.oportunia.presentation.ui.theme.pinkBackground
import com.example.oportunia.presentation.ui.theme.redBottomPart
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.RegisterViewModel
import com.example.oportunia.presentation.viewmodel.StartUpViewModel
import kotlinx.coroutines.launch

@Composable
fun StartUpScreen(
    startUpViewModel: StartUpViewModel,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterSuccess: () -> Unit,
) {
    val scope = rememberCoroutineScope()

    val selectedTab by startUpViewModel.selectedTab.collectAsState()

    // Estados relacionados al loginViewModel
    val loginUiState by loginViewModel.uiState.collectAsState()
    val loginEmail by loginViewModel.email.collectAsState()
    val loginPassword by loginViewModel.password.collectAsState()

    // Estados relacionados al registerViewModel
    val registerUiState by registerViewModel.uiState.collectAsState()
    val regEmail by registerViewModel.email.collectAsState()
    val regPassword by registerViewModel.password.collectAsState()
    val verifyRegPassword by registerViewModel.verifyPassword.collectAsState()
    val selectedRole by registerViewModel.selectedRole.collectAsState()

    LaunchedEffect(loginUiState) {
        if (loginUiState is LoginViewModel.LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    LaunchedEffect(registerUiState) {
        if (registerUiState is RegisterViewModel.RegisterUiState.Success) {
            onRegisterSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Primera fila: titulo de OportunIA e Iniciar sesión
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.weight(1.2f))

                Text(
                    text = "OportuniaIA",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Inicia sesión o crea una cuenta nueva",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.weight(1f))

                // Tabs de "Ingresar" y "Crear cuenta"
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(2f)
                ) {
                    TabItem(
                        text = "Ingresar",
                        isSelected = selectedTab == 0,
                        onClick = { startUpViewModel.setSelectedTab(0) },
                        shape = RoundedCornerShape(topEnd = 16.dp),
                        modifier = Modifier.weight(1f)
                    )

                    TabItem(
                        text = "Crear cuenta",
                        isSelected = selectedTab == 1,
                        onClick = { startUpViewModel.setSelectedTab(1) },
                        shape = RoundedCornerShape(topStart = 16.dp),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Segunda fila con el resto de cosas
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(pinkBackground)
                    .weight(4f),
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                // Login Tab
                if (selectedTab == 0) {
                    // Input para el correo / usuario
                    GeneralInput(
                        value = loginEmail,
                        onValueChange = { loginViewModel.onEmailChanged(it) },
                        iconResId = R.drawable.ic_user,
                        placeholderText = "Usuario o correo electrónico"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Input para la contraseña
                    GeneralInput(
                        value = loginPassword,
                        onValueChange = { loginViewModel.onPasswordChanged(it) },
                        iconResId = R.drawable.ic_lock,
                        placeholderText = "Contraseña",
                        isPasswordInput = true
                    )

                    Spacer(modifier = Modifier.height(33.dp))

                    CenteredDivider()

                    Spacer(modifier = Modifier.weight(.3f))

                    SocialLoginButton(
                        text = "¡Incluye inicio de sesión con Fingerprint!",
                        iconResId = R.drawable.ic_fingerprint,
                        onClick = { /* Fingerprint login */ }
                    )

                    Spacer(modifier = Modifier.weight(.3f))

                    CenteredDivider()

                    Spacer(modifier = Modifier.weight(.3f))

                    // Boton de login
                    ActionButton(
                        text = "Iniciar Sesión",
                        onClick = {
                            scope.launch {
                                loginViewModel.login(loginEmail, loginPassword)
                            }
                        },
                        isLoading = loginUiState is LoginViewModel.LoginUiState.Loading
                    )
                }
                if (selectedTab == 1) {
                    GeneralInput(
                        value = regEmail,
                        onValueChange = { registerViewModel.onEmailChanged(it) },
                        iconResId = R.drawable.ic_email,
                        placeholderText = "Correo electrónico"
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    GeneralInput(
                        value = regPassword,
                        onValueChange = { registerViewModel.onPasswordChanged(it) },
                        iconResId = R.drawable.ic_lock,
                        placeholderText = "Contraseña",
                        isPasswordInput = true
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Repite tu contraseña",
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 35.dp),
                        color = Color.Black,
                        fontStyle = FontStyle.Italic,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.weight(.9f))

                    GeneralInput(
                        value = verifyRegPassword,
                        onValueChange = { registerViewModel.onVerifyPasswordChanged(it) },
                        iconResId = R.drawable.ic_lock,
                        placeholderText = "Contraseña",
                        isPasswordInput = true
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    CenteredDivider()

                    Spacer(modifier = Modifier.height(25.dp))

                    GeneralDropdown(
                        optionsWithIcons = mapOf(
                            "Estudiante" to R.drawable.ic_student_icon,
                            "Empresa" to R.drawable.ic_company
                        ),
                        selectedOption = selectedRole,
                        onOptionSelected = { registerViewModel.onSelectedRoleChanged(it) },
                        placeholderText = "Seleccione su rol"
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    ActionButton(
                        text =  "Registrarme",
                        onClick = {
                            scope.launch {
                                registerViewModel.register()
                            }
                        },
                        isLoading = registerUiState is RegisterViewModel.RegisterUiState.Loading
                    )

                }
                Spacer(modifier = Modifier.weight(.3f))

                // Terminos y condiciones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Al iniciar sesión con una cuenta, aceptas los ",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Términos de Servicio",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* Handle terms */ }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "y la ",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = "Política de privacidad",
                        fontSize = 12.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { /* Handle privacy policy */ }
                    )
                    Text(
                        text = " de SO's",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom box on the nudge
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .background(redBottomPart)
                        .height(37.dp),
                    contentAlignment = Alignment.Center
                ) { }
            }
        }

        // Mensaje de error si se hace mal el login
        if (loginUiState is LoginViewModel.LoginUiState.Error) {
            ErrorPopUp (
                errorMessage = (loginUiState as LoginViewModel.LoginUiState.Error).message,
                onDismiss = { loginViewModel.resetErrorState() }
            )
        }

        if (loginUiState  is LoginViewModel.LoginUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

    }
}
