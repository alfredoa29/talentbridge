package com.example.oportunia.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.domain.model.Internship

import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.bottonBrownCompany
import com.example.oportunia.presentation.ui.theme.bottonEditBrownCompany
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel

@Composable
fun InternShipItemCompany(internship: Internship, onClick: (Internship) -> Unit, onToggleStatus: (Internship) -> Unit) {

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp) // Altura de la tarjeta
            .padding(3.dp)
            .clickable { /* aqui puedo poner que al hacer click en la card vaya a algun lado*/ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundCream// Fondo blanco para la tarjeta
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()

            ) {
                // Primera fila: Foto y datos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    // Foto
                    Box(
                        modifier = Modifier
                            .size(80.dp) // Tamaño de la foto
                            .background(Color.LightGray) // Color de fondo temporal
                    ) {

                        // Cargar la imagen usando Coil desde la URL
                        AsyncImage(
                            model = internship.company.pictureUrl,
                            contentDescription = "Logo de la pasantía",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop // Ajusta la imagen dentro del Box
                        )
                    }

                    // Datos (título y descripción)
                    Column(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .weight(1f) // Ocupa el espacio restante
                    ) {
                        // Título de la pasantía
                        Text(
                            text = internship.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Descripción de la pasantía
                        Text(
                            text = internship.shortDescription,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                ) {
                    // Botón "Más información" en la parte inferior
                    ElevatedButton(
                        onClick = { onClick(internship) },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            //    .align(Alignment.BottomEnd) // Alinea el botón en la parte inferior derecha
                            .width(80.dp) // Tamaño reducido del botón
                            .height(50.dp)
                            .padding(end = 5.dp),//padding derecho del boton
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundCream, // Color de fondo del botón
                            contentColor = bottonEditBrownCompany  // Color del texto del botón
                        ),
                        contentPadding = PaddingValues(
                            horizontal = 8.dp,
                            vertical = 2.dp
                        ) // Reduce el inner padding
                    ) {
                        Text(
                            text = "Editar ",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            // Llama una función que cambia el estado de habilitado/deshabilitado
                            showDialog = true
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, buttonBrownLine),
                        modifier = Modifier
                            .width(100.dp)
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = bottonBrownCompany,
                            contentColor = Color.White
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (internship.active) "Deshabilitar" else "Habilitar",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (showDialog) {
                        androidx.compose.material3.AlertDialog(
                            onDismissRequest = { showDialog = false },
                            title = { Text("Confirmación") },
                            text = { Text("¿Deseas confirmar la actualizacion de la pasantía?") },
                            confirmButton = {
                                Button(
                                    onClick = {
                                        showDialog = false
                                        val updatedInternship = internship.copy(active = !internship.active)
                                        onToggleStatus(updatedInternship) //



                                    }
,
                                ) {
                                    Text("Guardar")
                                }
                            },
                            dismissButton = {
                                Button(
                                    onClick = { showDialog = false }
                                ) {
                                    Text("Cancelar")
                                }
                            }
                        )
                    }


                }
            }
        }
    }
}