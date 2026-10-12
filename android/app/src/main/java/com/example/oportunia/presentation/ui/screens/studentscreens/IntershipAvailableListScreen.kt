package com.example.oportunia.presentation.ui.screens.studentscreens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.presentation.ui.components.InternshipItem
import com.example.oportunia.presentation.ui.layout.MainLayout
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.graphics.Color
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.presentation.viewmodel.InternshipState
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.StudentViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntershipAvailableListScreen(
    navController: NavController,
    internshipViewModel: InternshipViewModel,
    studentViewModel: StudentViewModel,
    sharedDataViewModel: SharedDataViewModel

    ) {
    val internshipList by internshipViewModel.internshipList.collectAsState()
    val internshipState by internshipViewModel.internship.collectAsState()

    val isLoading = internshipViewModel.isLoading.collectAsState().value

    val studentId by sharedDataViewModel.currentUser


    LaunchedEffect(studentId) {
        studentId?.let { id ->
            internshipViewModel.findAllInternshipsByStudentId(id)
        }
    }

    var isRefreshing by remember { mutableStateOf(false) }

    // Update refreshing state based on internsihp state

    isRefreshing = internshipState is InternshipState.Loading


    @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            studentId?.let { id ->
                isRefreshing = true
                internshipViewModel.findAllInternshipsByStudentId(id)
            } ?: Log.w("InternshipScreen", "studentId aún no está disponible")
        }
    )



    @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
    MainLayout(
        navController = navController,
        topBarTitle = stringResource(R.string.intership_title)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            when {
                internshipList.isEmpty() && internshipState is InternshipState.Loading -> {
                    LoadingContent()
                }

                internshipList.isEmpty() -> {
                    EmptyContent()
                }

                else -> {
                    InternshipListContent(
                        internshipList = internshipList,
                        navController = navController
                    )
                }
            }


            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
                backgroundColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopCenter)
            )
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


/**
 * Displays a list of tasks in a scrollable column.
 *
 * This composable is shown when tasks are available to display.
 * It demonstrates:
 * - LazyColumn for efficient scrolling lists
 * - Items with unique keys for better performance
 * - Click handling for navigation
 * - Proper semantics for list accessibility
 *
 * @param taskList The list of tasks to display
 * @param navController The navigation controller used to navigate to task details
 */
@Composable
private fun InternshipListContent(
    internshipList: List<Internship>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp)
         //   .padding(paddingValues) //aplicar este padding que creo que venia desde el main
        //pading de las tarjetas de listas de internship list screen

    ) {
        items(internshipList) { internship ->
            InternshipItem(internship = internship, onClick = {
                //  ruta definida en el data object para InternshipDetail
                navController.navigate(NavRoutes.InternshipDetail.createRoute(internship.id))
            })
        }
    }
}
