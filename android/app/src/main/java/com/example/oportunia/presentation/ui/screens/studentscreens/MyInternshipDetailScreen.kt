package com.example.oportunia.presentation.ui.screens.studentscreens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.layout.DetailsLayout
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream
import com.example.oportunia.viewmodel.StudentViewModel
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MyInternshipDetailScreen(
    internshipId: Long,
    studentViewModel: StudentViewModel,
    navController: NavController
) {
    LaunchedEffect(internshipId) {
        studentViewModel.selectInternshipById(internshipId)
    }


    val isLoading = studentViewModel.isLoading.collectAsState().value


    // Obtener la pasantía seleccionada
  //ver este val gabo lo tenia pero creo que yo lo habia cambiado por applicationstate
  //  val internship = studentViewModel.selectedInternship.collectAsState().value

    val applicationState = studentViewModel.selectedApplication.collectAsState().value


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
                        .fillMaxWidth()
                        .fillMaxSize()
                        .padding(horizontal = 2.dp, vertical = 2.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundCream)
                ) {
                    applicationState?.let { application ->
                        val internship = application.internship

                        Column {
                            // Fila con logo e info de empresa
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
                                        text = " ${internship.company.name}",
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

                            // Detalles
                            Text(
                                text = " ${internship.title}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(1.dp),
                                color = Color.Black
                            )
                            Text(
                                text = " Costa Rica",
                                fontSize = 15.sp,
                                modifier = Modifier.padding(1.dp),
                                color = Color.Black
                            )

                            Text(
                                text = " ${application.status.name}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(1.dp),
                                color = Color.Black
                            )
                            Text(
                                text = "Descripción: ${internship.description}",
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .padding(top = 30.dp, bottom = 30.dp),
                                color = Color.Gray
                            )

                            // Botón de volver
                            ElevatedButton(
                                onClick = { navController.navigateUp() },
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
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Volver a la lista",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                    } ?: run {
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
}
