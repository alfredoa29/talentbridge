package com.example.oportunia.presentation.ui.screens.studentscreens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.model.InterviewSection
import com.example.oportunia.presentation.ui.layout.DetailsLayout

import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream

import com.example.oportunia.viewmodel.InterviewState
import com.example.oportunia.viewmodel.StudentViewModel

@Composable
fun InterviewDetailScreen(
    internshipId: Long,
    studentViewModel: StudentViewModel,
    navController: NavController
) {
    // Generar la entrevista simulada al entrar
    LaunchedEffect(internshipId) {

        studentViewModel.generateInterview(internshipId)
        Log.d("InterviewDebug", "Valor de interviewState: $internshipId")
    }

    // Estado observable de la entrevista generada
    val interviewState = studentViewModel.interviewState.collectAsState().value

    val isLoading = studentViewModel.isLoading.collectAsState().value

    // Layout de detalles
    DetailsLayout(
        navController = navController,
        topBarTitle = stringResource(R.string.interviews_simulation),
        content = { layoutPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(layoutPadding)
                    .background(backgroundCream),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = backgroundCream)
                    ) {
                        when (interviewState) {
                            is InterviewState.Loading -> {
                                LoadingContent()
                            }

                            is InterviewState.Success -> {
                                val interview = interviewState.interview

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    val sections = remember(interviewState) {
                                        mutableStateListOf(
                                            InterviewSection("Introducción", interview.introduction.questions),
                                            InterviewSection("Evaluación de Conocimientos", interview.knowledgeAssessment.questions),
                                            InterviewSection("Resolución de Ejercicios", interview.problemSolving.exercises),
                                            InterviewSection("Expectativas y Cierre", interview.expectationsAndClosure.questions)
                                        )
                                    }

                                    sections.forEachIndexed { index, section ->
                                        ExpandableSection(section = section) {
                                            sections[index] = section.copy(isExpanded = !section.isExpanded)
                                        }
                                    }



                                    Spacer(modifier = Modifier.height(16.dp))

                                    ElevatedButton(
                                        onClick = { navController.navigateUp() },
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, buttonBrownLine),
                                        modifier = Modifier
                                            .align(Alignment.CenterHorizontally)
                                            .width(180.dp)
                                            .height(45.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = buttonCream,
                                            contentColor = Color.Black
                                        ),
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = "Volver a la lista",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            is InterviewState.Error -> {
                                Text(
                                    text = "Error al generar la entrevista: ${interviewState.message}",
                                    color = Color.Red,
                                    fontSize = 16.sp,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            else -> {
                                Text(
                                    text = "No se ha generado ninguna entrevista aún.",
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }
                }

                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillParentMaxSize()
                                .background(Color.Black.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White)
                        }
                    }
                }
            }
        }
    )

}

/**
 * Displays a loading indicator centered on the screen.
 *
 * This composable is shown when tasks are being fetched from the data source.
 * It demonstrates proper use of:
 * - Box layout with center alignment
 * - CircularProgressIndicator for loading state
 * - Semantic properties for accessibility
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.semantics {
                contentDescription = "Loading internships"
            }
        )
    }
}


/**
 * Displays a message when no tasks are available.
 *
 * This composable is shown when the task list is empty.
 * It demonstrates:
 * - Text styling with MaterialTheme
 * - String resources usage
 * - Proper semantics for accessibility
 * - TextAlign for centered text
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(id = R.string.no_internships_available),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .semantics {
                    contentDescription = "Empty internship list message"
                }
        )
    }
}



@Composable
fun ExpandableSection(
    section: InterviewSection,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = section.title,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f)
            )
            androidx.compose.material.Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(if (section.isExpanded) 180f else 0f)
            )
        }

        if (section.isExpanded) {
            Spacer(modifier = Modifier.height(8.dp))
            section.questions.forEachIndexed { i, q ->
                Text(text = "${i + 1}. $q", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
