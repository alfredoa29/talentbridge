package com.example.oportunia.presentation.ui.screens.studentscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.presentation.ui.components.MyInternshipItem
import com.example.oportunia.presentation.ui.layout.MainLayout
import com.example.oportunia.viewmodel.StudentViewModel
import androidx.compose.material3.CircularProgressIndicator

import androidx.compose.ui.graphics.Color

import com.example.oportunia.domain.model.Application
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.ApplicationState


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun MyInternshipsScreen(
    navController: NavController,
    studentViewModel: StudentViewModel,
    sharedDataViewModel: SharedDataViewModel,
    modifier: Modifier = Modifier
) {


    // Observar la lista de aplicaciones
    val applicationList by studentViewModel.applicationList.collectAsState()
    val studentApplicationState by studentViewModel.application.collectAsState()


    val isLoading = studentViewModel.isLoading.collectAsState().value

    val studentId by sharedDataViewModel.currentUser
    // Cargar pasantías al entrar
    LaunchedEffect(studentId) {
        studentId?.let {
            studentViewModel.getInternshipsByStudent(it)
        }
    }

    var isRefreshing by remember { mutableStateOf(false) }

    // Update refreshing state based on task state
    isRefreshing =  studentApplicationState is  ApplicationState.Loading


    @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            studentId?.let {
                studentViewModel.getInternshipsByStudent(it)
            }
        }
    )

    MainLayout(
        navController = navController,
        topBarTitle = stringResource(R.string.my_internships_title)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pullRefresh(pullRefreshState)
        ) {
            when {
                applicationList.isEmpty() && studentApplicationState is ApplicationState.Loading -> {
                    LoadingContent()
                }

                applicationList.isEmpty() -> {
                    EmptyContent()
                }

                else -> {
                    InternshipListContent(
                        applicationList = applicationList,
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
 * Displays a list of internships in a scrollable column.
 */
@Composable
private fun InternshipListContent(
    applicationList: List<Application>,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp)
    ) {
        items(applicationList) { application ->
            MyInternshipItem(application = application, onClick = {
                navController.navigate(NavRoutes.MyInternshipDetail.createRoute(application.id))
            })
        }
    }
}
