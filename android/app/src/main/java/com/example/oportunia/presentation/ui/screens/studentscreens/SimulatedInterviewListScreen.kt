

package com.example.oportunia.presentation.ui.screens.studentscreens


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.*

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.presentation.ui.components.InterviewItem
import com.example.oportunia.presentation.ui.layout.MainLayout
import com.example.oportunia.viewmodel.ApplicationState
import com.example.oportunia.viewmodel.StudentViewModel
import com.example.oportunia.domain.model.Application
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun SimulatedInterviewListScreen(
    navController: NavController,
    studentViewModel: StudentViewModel,
    sharedDataViewModel: SharedDataViewModel,
    modifier: Modifier = Modifier,


    ) {
    val applicationList by studentViewModel.applicationList.collectAsState()
    val studentApplicationState by studentViewModel.application.collectAsState()

    val studentId by sharedDataViewModel.currentUser

    val isLoading = studentViewModel.isLoading.collectAsState().value

    LaunchedEffect(studentId) {
        studentId?.let {
            studentViewModel.getInternshipsByStudent(it)
        }
    }

    var isRefreshing by remember { mutableStateOf(false) }
    isRefreshing = studentApplicationState is ApplicationState.Loading

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
        topBarTitle = stringResource(R.string.interviews_simulation)
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
                    InterviewListContent(
                        applicationList = applicationList,
                        navController = navController,
                        isLoading = isLoading

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
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.semantics {
                contentDescription = "Loading simulated interviews"
            }
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_simulated_interviews),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .semantics {
                    contentDescription = "Empty simulated interview list"
                }
        )
    }
}

@Composable
private fun InterviewListContent(
    applicationList: List<Application>,
    navController: NavController,
    isLoading: Boolean
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(applicationList) { application ->
                InterviewItem(application = application) {
                    Log.d("applicationDebug", "Valor de application: $application")
                    navController.navigate(NavRoutes.InterviewDetail.createRoute(application.internship.id))
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
}

