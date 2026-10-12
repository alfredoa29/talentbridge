package com.example.oportunia.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavHostController
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.presentation.ui.screens.studentscreens.CVAnalysisDetailScreen

import com.example.oportunia.presentation.ui.screens.studentscreens.InternshipDetailScreen

import com.example.oportunia.presentation.ui.screens.studentscreens.CVAnalysisScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.IntershipAvailableListScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.InterviewDetailScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.MyInternshipDetailScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.MyInternshipsScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.SimulatedInterviewListScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.StudentProfileDetailScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.StudentProfileScreen
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.StudentViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    internshipViewModel: InternshipViewModel,
    sharedDataViewModel: SharedDataViewModel,
    studentViewModel: StudentViewModel,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
    authPreferences: AuthPreferences
) {

    /*
    // Simular que ya hay un estudiante logueado
    LaunchedEffect(Unit) {
        studentViewModel.setStudentId(1)  // Quemar el ID aquí
    }
     */


    // Observar el studentId desde el ViewModel
    val studentId by sharedDataViewModel.currentUser

    NavHost(
        navController = navController,
        startDestination = NavRoutes.InternshipList.ROUTE,
        modifier = modifier
    ) {




        // Pantalla de lista de pasantías
        composable(NavRoutes.InternshipList.ROUTE) {
            IntershipAvailableListScreen(
                navController = navController,
                internshipViewModel = internshipViewModel,
                studentViewModel =  studentViewModel,
                sharedDataViewModel =  sharedDataViewModel
            )
        }


        composable(NavRoutes.CVAnalysis.ROUTE) {
            // Pantalla de análisis de CV (a implementar)
            CVAnalysisScreen(navController =  navController,
                studentViewModel = studentViewModel

                )
        }

        // Pantalla de mis pasantías
        composable(
            route = NavRoutes.MyInternships.ROUTE
        ) {
            MyInternshipsScreen(
                navController = navController,
                studentViewModel = studentViewModel,
                sharedDataViewModel =  sharedDataViewModel
            )
        }

        composable(NavRoutes.Interviews.ROUTE) {


            // Pantalla de entrevistas (a implementar)
            SimulatedInterviewListScreen(
                navController = navController,
                studentViewModel = studentViewModel,
                sharedDataViewModel =  sharedDataViewModel


            )


        }
        composable(NavRoutes.InternshipDetail.ROUTE,
            arguments = listOf(navArgument(NavRoutes.InternshipDetail.ARG_TASK_ID){
                type = NavType.LongType
            })

            ) { backStackEntry ->
            val internshipId = backStackEntry.arguments?.getLong(NavRoutes.InternshipDetail.ARG_TASK_ID)?: 0L
                InternshipDetailScreen(
                    internshipId = internshipId,
                    internshipViewModel = internshipViewModel,
                    navController = navController,
                    sharedDataViewModel =  sharedDataViewModel
                )

        }


        composable(NavRoutes.MyInternshipDetail.ROUTE,
            arguments = listOf(navArgument(NavRoutes.MyInternshipDetail.ARG_TASK_ID){
                type = NavType.LongType
            })

        ) { backStackEntry ->
            val internshipId = backStackEntry.arguments?.getLong(NavRoutes.MyInternshipDetail.ARG_TASK_ID)?: 0L
            MyInternshipDetailScreen(
                internshipId = internshipId,
                studentViewModel = studentViewModel,
                navController = navController
            )

        }



        composable(NavRoutes.InterviewDetail.ROUTE,
            arguments = listOf(navArgument(NavRoutes.InterviewDetail.ARG_TASK_ID){
                type = NavType.LongType
            })

        ) { backStackEntry ->
            val internshipId = backStackEntry.arguments?.getLong(NavRoutes.InterviewDetail.ARG_TASK_ID)?: 0L


                InterviewDetailScreen(
                    internshipId = internshipId,
                    studentViewModel = studentViewModel,
                    navController = navController

                )



        }

        composable(NavRoutes.StudentProfile.ROUTE) {
            // Pantalla de perfil de estudiante   (a implementar)
            StudentProfileScreen(navController = navController,
                studentViewModel = studentViewModel,
                sharedDataViewModel =  sharedDataViewModel,
                loginViewModel = loginViewModel,
                authPreferences = authPreferences
                )
        }

        composable(NavRoutes.CVanalysisdetail.ROUTE) { backStackEntry ->

                CVAnalysisDetailScreen(
                    navController = navController,
                    studentViewModel = studentViewModel

                )

        }


        composable(NavRoutes.StudentProfileDetail.ROUTE,
            arguments = listOf(navArgument(NavRoutes.StudentProfileDetail.ARG_TASK_ID){
            type = NavType.LongType
        })){ backStackEntry ->
            val studentId = backStackEntry.arguments?.getLong(NavRoutes.StudentProfileDetail.ARG_TASK_ID)?: 0L
            StudentProfileDetailScreen(
                studentId = studentId,
                navController = navController,
                studentViewModel = studentViewModel,
                sharedDataViewModel = sharedDataViewModel)
        }



    }
}



