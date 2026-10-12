package com.example.oportunia.presentation.ui.screens.studentscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.layout.DetailsLayout
import com.example.oportunia.presentation.ui.model.InterviewSection
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.buttonCream
import com.example.oportunia.presentation.ui.theme.cardCVColor
import com.example.oportunia.presentation.ui.theme.cardCVColorBrown
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.viewmodel.StudentViewModel

@Composable
fun CVAnalysisDetailScreen(
    studentViewModel: StudentViewModel,
    navController: NavController,
) {
    val analysis = studentViewModel.currentAnalysis.value

    DetailsLayout(
        navController = navController,
        topBarTitle = stringResource(R.string.cv_analysis),
    ) { layoutPadding ->

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
                    if (analysis == null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay análisis disponible.",
                                color = Color.Gray,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        // Secciones para cada atributo de AnalizeCurriculumAI
                        val sections = remember(analysis) {
                            mutableStateListOf(
                                InterviewSection("Fortalezas", analysis.strengths),
                                InterviewSection("Debilidades", analysis.weaknesses),
                                InterviewSection("Áreas de mejora", analysis.improvementAreas),
                            )
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            sections.forEachIndexed { index, section ->
                                ExpandableSectionSimple(
                                    section = section,
                                    onClick = {
                                        sections[index] = section.copy(isExpanded = !section.isExpanded)
                                    }
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Evaluación General",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Text(
                                text = analysis.overallAssessment,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            ElevatedButton(
                                onClick = { "studentViewModel.regenerateAnalysis()" },
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
                                    text = "Volver a generar",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableSectionSimple(
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
            if (section.questions.isEmpty()) {
                Text(
                    text = "No hay datos disponibles.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            } else {
                section.questions.forEachIndexed { i, q ->
                    Text(text = "• $q", fontSize = 14.sp, color = Color.Black)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
    }
}
