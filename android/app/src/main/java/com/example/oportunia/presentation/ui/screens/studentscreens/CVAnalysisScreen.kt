package com.example.oportunia.presentation.ui.screens.studentscreens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.layout.MainLayout
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.cardCVColor
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.viewmodel.StudentViewModel
import androidx.compose.ui.platform.LocalContext
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.viewmodel.CVUploadState
import com.example.oportunia.viewmodel.FileSelectionState


@Composable
fun CVAnalysisScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    studentViewModel: StudentViewModel
) {
    val selectedUri by studentViewModel.selectedFileUri.collectAsState()
    val uploadState by studentViewModel.uploadResult.collectAsState()
    val fileSelectionState by studentViewModel.fileSelectionState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        studentViewModel.resetFileSelectionState()
        studentViewModel.resetUploadState()
        studentViewModel.clearSelectedFileUri()
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            // Pide permiso persistente para este URI
            context.contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            studentViewModel.setSelectedFileUri(uri)
        }
    }

    MainLayout(navController = navController,
        topBarTitle = stringResource(R.string.cv_analysis)) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ... (primer Card omitido para brevedad)

            // Segunda tarjeta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(5.dp),
                colors = CardDefaults.cardColors(containerColor = cardCVColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "application/pdf"
                            }
                            val chooser = Intent.createChooser(intent, "Selecciona un archivo PDF")
                            launcher.launch(chooser)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, buttonBrownLine),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red810,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cargar CV", fontSize = 20.sp)
                    }

                    Button(
                        onClick = {
                            if (selectedUri != null) {
                                studentViewModel.uploadCV(selectedUri!!, context.contentResolver)
                            } else {
                                Toast.makeText(context, "Selecciona un archivo antes", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, buttonBrownLine),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red810,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Analizar CV", fontSize = 20.sp)
                    }

                    // Mostrar mensaje cuando archivo seleccionado
                    if (fileSelectionState is FileSelectionState.FileSelected) {
                        Toast.makeText(context, "Archivo seleccionado con éxito", Toast.LENGTH_SHORT).show()

                        // Limpiar el mensaje después de 2 segundos
                        LaunchedEffect(Unit) {
                            kotlinx.coroutines.delay(2000)
                            studentViewModel.resetFileSelectionState()
                        }
                    }

                    // Mostrar estado de carga / éxito / error
                    when (uploadState) {
                        is CVUploadState.Loading -> {
                            Text("Analizando CV...", color = Color.Gray)
                        }
                        is CVUploadState.Success -> {
                            LaunchedEffect(Unit) {
                                navController.navigate(NavRoutes.CVanalysisdetail.ROUTE)
                            }
                        }
                        is CVUploadState.Error -> {
                            Text("Error: ${(uploadState as CVUploadState.Error).message}")
                        }
                        CVUploadState.Idle -> Unit
                        else -> {}
                    }
                }
            }
        }
    }
}

