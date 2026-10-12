package com.example.oportunia.presentation.ui.screens.studentscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.oportunia.R
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.domain.model.Student
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.presentation.ui.layout.DetailsLayout
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

@Composable
fun StudentProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    sharedDataViewModel: SharedDataViewModel,
    studentViewModel: StudentViewModel,
    loginViewModel: LoginViewModel,
    authPreferences: AuthPreferences
) {

    val isLoading by studentViewModel.isLoading.collectAsState()
    val selectedStudent by studentViewModel.selectedStudent.collectAsState()

    val studentId by sharedDataViewModel.currentUser

    LaunchedEffect(studentId) {
        studentId?.let {
            studentViewModel.selectStudentById(it)
        }
    }




    DetailsLayout (
        navController = navController,
        topBarTitle = stringResource(R.string.student_profile_title)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                // Primera tarjeta (arriba)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundCream),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        ProfileContent(student = selectedStudent, navController = navController, loginViewModel = loginViewModel, authPreferences =  authPreferences)

                    }
                }

                // Círculo de carga
                if (isLoading) {
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
    }
}

@Composable
fun BiometricSettingsScreen(authPreferences: AuthPreferences, ) {
    val scope = rememberCoroutineScope()
    var biometricEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        biometricEnabled = authPreferences.isBiometricEnabled()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Autenticación biométrica", style = MaterialTheme.typography.titleLarge, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    authPreferences.setBiometricEnabled(!biometricEnabled)
                    biometricEnabled = !biometricEnabled
                }
            }
        ) {
            Text(
                if (biometricEnabled) "Desactivar biometría" else "Activar biometría"
            )
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfileContent(student: Student?, navController: NavController, loginViewModel: LoginViewModel, authPreferences: AuthPreferences) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (student == null) {
            Text("Cargando estudiante...", modifier = Modifier.padding(16.dp))
            return
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = student.fullName ?: "Nombre no disponible",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        // Imagen de perfil (ícono por defecto)
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {

            AsyncImage(
                model = student.profilePicUrl,
                contentDescription = "Profile Picture",
                modifier = Modifier.matchParentSize(), // Asegura que llene el círculo
                contentScale = ContentScale.Crop
            )


        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logout button
        Button(
            onClick = {
                loginViewModel.logout()

                      },
            colors = ButtonDefaults.buttonColors(containerColor = red810),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        BiometricSettingsScreen(authPreferences = authPreferences )


        Spacer(modifier = Modifier.height(16.dp))


        // Información del estudiante
        InfoCard(title = "Nombre completo ", value = student.fullName)
        InfoCard(title = "Nombre de usuario", value = student.username ?: "No disponible")
        InfoCard(title = "País de residencia", value = student.country.name ?: "No disponible")
        InfoCard(title = "Género", value = student.gender.name ?: "No disponible")
        InfoCard(title = "Correo electrónico", value = student.email ?: "No disponible")
        InfoCard(title = "Universidad ", value = student.university)
        InfoCard(title = "Carrera ", value = student.career)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Habilidades:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        FlowRow(

            modifier = Modifier.fillMaxWidth()
        ) {
            if (student.skillList.isNotEmpty()) {
                student.skillList.forEach { skill ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = skill.name, fontSize = 14.sp, color = Color.Black)
                    }
                }
            } else {
                Text("Sin habilidades registradas", fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = " Areas de interes:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        FlowRow(

            modifier = Modifier.fillMaxWidth()
        ) {
            if (student.areaOfInterestList.isNotEmpty()) {
                student.areaOfInterestList.forEach { skill ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = skill.name, fontSize = 14.sp, color = Color.Black)
                    }
                }
            } else {
                Text("Sin areas de interes registradas", fontSize = 14.sp, color = Color.Gray)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Settings button
        Button(
            onClick = {navController.navigate(NavRoutes.StudentProfileDetail.createRoute(student.id))  },
            colors = ButtonDefaults.buttonColors(containerColor = red810),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Configuración",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


@Composable
fun InfoCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
}
