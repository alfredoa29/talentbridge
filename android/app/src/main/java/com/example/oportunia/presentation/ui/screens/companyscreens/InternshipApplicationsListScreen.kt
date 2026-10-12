package com.example.oportunia.presentation.ui.screens.companyscreens

import android.util.Log
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.domain.model.StudentInternship
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.presentation.ui.components.InternshipApplicationItem
import com.example.oportunia.presentation.ui.layout.MainLayoutCompany
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.presentation.viewmodel.StudentInternshipState
import com.example.oportunia.viewmodel.StudentViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InternshipApplicationsListScreen(
    navController: NavController,
    studentViewModel: StudentViewModel,
    sharedDataViewModel: SharedDataViewModel,
    companyViewModel: CompanyViewModel

    ) {
    val studentInternshipList by  companyViewModel.studentInternshipList.collectAsState()
    val studentInternshipState by companyViewModel.studentInternship.collectAsState()

    var showDialog by remember { mutableStateOf(false) }




    var isRefreshing by remember { mutableStateOf(false) }

    // Update isRefreshing when taskState changes
    LaunchedEffect(studentInternshipState) {
        isRefreshing = studentInternshipState is StudentInternshipState.Loading
    }

    val currentUser by sharedDataViewModel.currentUser
    Log.d("InternshipApplicationsListScreen", "Company selected: $currentUser")
    LaunchedEffect(currentUser) {
        currentUser?.let {
            companyViewModel.selectCompanyById(it)
            companyViewModel.getStudentsAndInternshipsByCompany(it)
        }
    }


    @OptIn(androidx.compose.material.ExperimentalMaterialApi::class)
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            currentUser?.let {
                companyViewModel.getStudentsAndInternshipsByCompany(it)
            }
        }
    )


    MainLayoutCompany(navController = navController,
        topBarTitle = stringResource(R.string.applicants_internship_title)
    ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .padding(paddingValues)
                    .pullRefresh(pullRefreshState), //aplicar este padding que creo que venia desde el main
                    contentAlignment = Alignment.Center
            )  {
                when {
                    studentInternshipList.isEmpty() && studentInternshipState is StudentInternshipState.Loading -> {
                        LoadingContent()
                    }

                    studentInternshipList.isEmpty() -> {
                        EmptyContent()
                    }
                    else -> {
                        StudentInternshipsListContent(
                            studentInternshipsList = studentInternshipList,
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

    }


}

/**
 * Displays a loading indicator centered on the screen.
 *
 * This composable is shown when studentsinternships are being fetched from the data source.
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
                contentDescription = "Loading applications"
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
            text = stringResource(id = R.string.no_students_internship_available),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .semantics {
                    contentDescription = "Empty Students list message"
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
private fun StudentInternshipsListContent(
    studentInternshipsList: List<StudentInternship>,
    navController: NavController
){
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 0.dp)
           // .padding(paddingValues) //aplicar este padding que creo que venia desde el main
        //pading de las tarjetas de listas de internship list screen

    ) {

        items(studentInternshipsList) { studentInternship ->
            InternshipApplicationItem(studentInternship = studentInternship, onClick = {

                navController.navigate(NavRoutes.InternshipApplicationDetail.createRoute(studentInternship.applicationId))
            })
        }

    }


}