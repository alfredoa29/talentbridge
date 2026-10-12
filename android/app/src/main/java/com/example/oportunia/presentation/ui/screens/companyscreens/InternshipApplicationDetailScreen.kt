package com.example.oportunia.presentation.ui.screens.companyscreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.layout.DetailsLayoutCompany
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.ui.components.AlertDialog
import com.example.oportunia.presentation.ui.theme.bottonBrownCompany
import com.example.oportunia.presentation.ui.theme.bottonEditBrownCompany
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.UpdateApplicationState
import com.example.oportunia.viewmodel.StudentViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InternshipApplicationDetailScreen(
    applicationId: Long,
    companyViewModel: CompanyViewModel,
    internshipViewModel:  InternshipViewModel,
    navController: NavController
){

    LaunchedEffect(applicationId) {
        Log.i("IMPRIMIENDO_APPLICATION_ID", "applicationId = ${ applicationId}")
        companyViewModel.getApplicationById(applicationId)
    }

    val selectedApplication by companyViewModel.selectedtApplication.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val openAlertDialog = remember { mutableStateOf(false) }


    val shouldShowSnackbar = remember { mutableStateOf(false) }

    val updateState by companyViewModel.updateApplicationState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(updateState) {
        when (updateState) {
            is UpdateApplicationState.Success -> {
                companyViewModel.getApplicationById(applicationId)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Estado actualizado correctamente")
                }
            }
            is UpdateApplicationState.Error -> {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Error al actualizar: ${(updateState as UpdateApplicationState.Error).errorMessage}")
                }
            }
            else -> {}
        }
    }

    DetailsLayoutCompany(navController = navController,
        topBarTitle = stringResource(R.string.applicant_details),
        internshipViewModel = internshipViewModel,
        content = { layoutPadding ->

            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(layoutPadding)
                    .padding(16.dp)
                    .background(backgroundCream)
            )  {
                Card (
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()// Altura de la tarjeta
                        .padding(horizontal = 2.dp, vertical = 2.dp),

                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = backgroundCream// Fondo blanco para la tarjeta
                    )
                )
                {


                    selectedApplication?.let {

                        Column( modifier = Modifier) {

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .padding(10.dp)// Tamaño de la foto
                                    //  .background(Color.LightGray) // Color de fondo temporal
                                ) {

                                    // Cargar la imagen usando Coil desde la URL

                                        AsyncImage(
                                            model = it.student.profilePicUrl,
                                            contentDescription = "Logo de la pasantía",
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop // Ajusta la imagen dentro del Box
                                        )

                                }
                                Column {
                                    Text(
                                        text = " ${it.student.fullName}", // Título
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(1.dp),
                                        color = Color.Black
                                    )


                                }
                            }
                            FlowRow(
                                modifier = Modifier.padding(4.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                maxItemsInEachRow = 2
                            ) {
                                val itemModifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                Box(
                                    modifier = itemModifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                    //.background(Color.Red)
                                )
                            }


                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .background(backgroundCream)
                                    ,
                                shape = RoundedCornerShape(12.dp),


                            ) {



                                Column(
                                    modifier = Modifier.background(backgroundCream).padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    LabelValueRow("Pasante:", it.student.fullName)
                                    LabelValueRow("País:", it.student.country.name, Color.Gray)
                                    LabelValueRow("Carrera:", it.student.career)

                                    Text(
                                        text = "Habilidades:",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Light,
                                        color = Color.Gray
                                    )
                                    FlowRow(

                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        if (it.student.skillList.isNotEmpty()) {
                                            it.student.skillList.forEach { skill ->
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

                                    LabelValueRow("Título de la pasantía:", it.internship.title)
                                    LabelValueRow("Descripción de la pasantía:", it.internship.shortDescription)
                                    LabelValueRow("Estado de la aplicacion:", it.status.name)
                                }
                            }

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

                            val openAlertDialog = remember { mutableStateOf(false) }
                            val actionToConfirm = remember { mutableStateOf("") } // "approve" o "reject"
                            // Botón para regresar a la lista de pasantías

                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),){
                                // Botón Aprobar
                                ElevatedButton(
                                    onClick = {
                                        actionToConfirm.value = "approve"
                                        openAlertDialog.value = true
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Color.Black),
                                    modifier = Modifier
                                        //    .align(Alignment.BottomEnd) // Alinea el botón en la parte inferior derecha
                                        .width(80.dp) // Tamaño reducido del botón
                                        .height(50.dp)
                                        .padding( 8.dp),//padding derecho del boton
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = backgroundCream, // Color de fondo del botón
                                        contentColor = bottonEditBrownCompany  // Color del texto del botón
                                    ),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Aprobar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Botón Rechazar
                                ElevatedButton(
                                    onClick = {
                                        actionToConfirm.value = "reject"
                                        openAlertDialog.value = true
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, buttonBrownLine),
                                    modifier = Modifier
                                        //      .align(Alignment.BottomEnd) // Alinea el botón en la parte inferior derecha
                                        .width(100.dp) // Tamaño reducido del botón
                                        .height(50.dp)
                                        .padding(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = bottonBrownCompany, // Color de fondo del botón
                                        contentColor = Color.White  // Color del texto del botón
                                    ),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = "Rechazar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Diálogo de confirmación
                                if (openAlertDialog.value) {
                                    AlertDialog(
                                        onDismissRequest = { openAlertDialog.value = false },
                                        onConfirmation = {
                                            openAlertDialog.value = false

                                            when (actionToConfirm.value) {
                                                "approve" -> companyViewModel.changeApplicationStatus(it.id, 2)  // 2 = aprobado
                                                "reject" -> companyViewModel.changeApplicationStatus(it.id, 3)   // 3 = rechazado
                                            }

                                            shouldShowSnackbar.value = true
                                        },
                                        dialogTitle = if (actionToConfirm.value == "approve") "Confirmar Aplicación" else "Confirmar Rechazo",
                                        dialogText = if (actionToConfirm.value == "approve")
                                            "¿Deseas aceptar a este pasante?"
                                        else
                                            "¿Deseas rechazar a este pasante?",
                                        icon = Icons.Default.Info,
                                    )
                                }
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

            /*
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



             */

        }
        )
}
@Composable
fun LabelValueRow(label: String, value: String, valueColor: Color = Color.Black) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Light,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            modifier = Modifier.weight(2f)
        )
    }
}
