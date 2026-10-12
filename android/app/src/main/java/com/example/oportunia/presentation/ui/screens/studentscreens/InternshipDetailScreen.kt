package com.example.oportunia.presentation.ui.screens.studentscreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.components.AlertDialog
import com.example.oportunia.presentation.ui.layout.DetailsLayout
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream
import com.example.oportunia.presentation.viewmodel.ApplicationState
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InternshipDetailScreen(
    internshipId: Long,
    internshipViewModel: InternshipViewModel,
    sharedDataViewModel: SharedDataViewModel,
    navController: NavController
) {
    // Ejecuta esta lógica solo cuando internshipId cambie
    LaunchedEffect(internshipId) {
        internshipViewModel.selectInternshipById(internshipId)
    }

    val internship = internshipViewModel.selectedInternship.collectAsState().value
    val snackbarHostState = remember { SnackbarHostState() }
    val openAlertDialog = remember { mutableStateOf(false) }

    // Observamos el estado de la aplicación
    val applicationState = internshipViewModel.applicationState.collectAsState().value
    val shouldShowSnackbar = remember { mutableStateOf(false) }

    val studentId by sharedDataViewModel.currentUser
    val scrollState = rememberScrollState()

    LaunchedEffect(applicationState, shouldShowSnackbar.value) {
        if (shouldShowSnackbar.value) {
            when (applicationState) {
                is ApplicationState.Success -> {
                    snackbarHostState.showSnackbar("Aplicación enviada con éxito")
                    shouldShowSnackbar.value = false
                }
                is ApplicationState.Error -> {
                    snackbarHostState.showSnackbar("Error al aplicar: ${applicationState.errorMessage}")
                    shouldShowSnackbar.value = false
                }
                else -> {}
            }
        }
    }

    val isLoading = internshipViewModel.isLoading.collectAsState().value


    // Contenido de la pantalla
    DetailsLayout(
                navController = navController,
                topBarTitle = stringResource(R.string.intership_details),

                content = { layoutPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(layoutPadding)
                            .padding(1.dp)
                            .background(backgroundCream)
                    ) {

                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 2.dp, vertical = 2.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = backgroundCream)
                        ) {
                            internship?.let {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState)
                                        .padding(8.dp)
                                ) {
                                    // ✅ Aquí sigue tu Row sin cambios
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .padding(10.dp)
                                        ) {
                                            AsyncImage(
                                                model = internship.company.pictureUrl,
                                                contentDescription = "Logo de la pasantía",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = " ${it.company.name}",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(1.dp),
                                                color = Color.Black
                                            )
                                        }
                                    }
                                    /*
                                    FlowRow(
                                        modifier = Modifier.padding(4.dp),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        maxItemsInEachRow = 3
                                    ) {
                                        val itemModifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                        Box(
                                            modifier = itemModifier
                                                .height(120.dp)
                                                .fillMaxWidth()
                                            //.background(Color.Red)
                                        )
                                    }

                                     */

                                    // Muestra los detalles de la pasantía
                                    Text(
                                        text = " ${it.title}", // Título
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(1.dp),
                                        color = Color.Black
                                    )
                                    Text(
                                        text = " Costa Rica", // Título
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(1.dp),
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Descripción: ${it.description}",
                                        fontSize = 15.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp)
                                            .padding(top = 30.dp, bottom = 30.dp),
                                        color = Color.Gray
                                    )

                                    // Botón para regresar a la lista de pasantías
                                    /*
                                    ElevatedButton(
                                        onClick = { navController.navigateUp() },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, buttonBrownLine),
                                        modifier = Modifier
                                            .width(130.dp)
                                            .height(30.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = buttonCream,
                                            contentColor = Color.Black
                                        ),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Volver a la lista",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                     */
                                    // Botón para regresar a la lista de pasantías
                                    ElevatedButton(
                                        onClick = { openAlertDialog.value = true },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, buttonBrownLine),
                                        modifier = Modifier
                                            .width(130.dp)
                                            .height(30.dp)
                                            .padding(start = 5.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = buttonCream,
                                            contentColor = Color.Black
                                        ),
                                        contentPadding = PaddingValues(
                                            horizontal = 8.dp,
                                            vertical = 2.dp
                                        )
                                    ) {
                                        Text(
                                            text = "Aplicar",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    if (openAlertDialog.value) {
                                        AlertDialog(
                                            onDismissRequest = { openAlertDialog.value = false },
                                            onConfirmation = {
                                                openAlertDialog.value = false
                                                studentId?.let { id ->
                                                    internshipViewModel.applyToInternship(
                                                        internshipId = internshipId,
                                                        studentId = id
                                                    )
                                                    shouldShowSnackbar.value = true
                                                } ?: run {
                                                    // mostrar un mensaje de error o log para avisar que studentId no está listo
                                                    Log.w(
                                                        "IntershipScreen",
                                                        "studentId no disponible al aplicar a la pasantía"
                                                    )
                                                }
                                            },
                                            dialogTitle = "Confirmar Aplicación",
                                            dialogText = "¿Deseas aplicar a esta pasantía?",
                                            icon = Icons.Default.Info,

                                            )
                                    }
                                }


                            } ?: run {
                                // Si no se encuentra la pasantía, muestra un mensaje
                                Text(
                                    text = "Pasantía no encontrada",
                                    fontSize = 20.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                    }
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
            )
    SnackbarHost(hostState = snackbarHostState)
                }

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.semantics {
                contentDescription = "Loading"
            }
        )
    }
}