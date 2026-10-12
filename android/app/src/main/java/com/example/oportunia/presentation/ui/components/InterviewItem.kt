package com.example.oportunia.presentation.ui.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.oportunia.domain.model.Application
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream

@Composable
fun InterviewItem(application: Application, onClick: (Application) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp) // Altura de la tarjeta
            .padding(8.dp)
            .clickable { onClick(application) },
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
                            .size(100.dp) // Tamaño de la foto
                        //  .background(Color.LightGray) // Color de fondo temporal
                    ) {

                        // Cargar la imagen usando Coil desde la URL
                        AsyncImage(
                            model = application.internship.company.pictureUrl,
                            contentDescription = "Logo de la pasantía",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop // Ajusta la imagen dentro del Box
                        )
                    }
                    // Datos (título y descripción)
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f) // Ocupa el espacio restante
                    ) {
                        // Título de la pasantía
                        Text(
                            text = application.internship.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        // nombre de la empresa- compania
                        Text(
                            text = application.internship.company.name,
                            fontSize = 14.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )


                        // Descripción de la pasantía
                        Text(
                            text = application.internship.shortDescription,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // Botón "Más información" en la parte inferior
            ElevatedButton(
                onClick = { onClick(application) },
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, buttonBrownLine),
                modifier = Modifier
                    .align(Alignment.BottomEnd) // Alinea el botón en la parte inferior derecha
                    .width(130.dp) // Tamaño reducido del botón
                    .height(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonCream, // Color de fondo del botón
                    contentColor = Color.Black  // Color del texto del botón
                ),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp) // Reduce el inner padding
            ) {
                Text(
                    text = "Simular entrevista",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}