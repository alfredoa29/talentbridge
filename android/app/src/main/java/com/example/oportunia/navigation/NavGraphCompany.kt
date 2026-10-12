package com.example.oportunia.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.oportunia.presentation.ui.screens.companyscreens.AccountScreen
import com.example.oportunia.presentation.ui.screens.companyscreens.CompanyProfileScreen
import com.example.oportunia.presentation.ui.screens.companyscreens.EditInternshipDetailScreen
import com.example.oportunia.presentation.ui.screens.companyscreens.InternshipApplicationDetailScreen
import com.example.oportunia.presentation.ui.screens.companyscreens.InternshipApplicationsListScreen
import com.example.oportunia.presentation.ui.screens.companyscreens.InternshipListScreen
import com.example.oportunia.presentation.ui.screens.companyscreens.RegisterInternshipScreen
import com.example.oportunia.presentation.ui.screens.studentscreens.StudentProfileDetailScreen
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel

import com.example.oportunia.viewmodel.StudentViewModel

@Composable
fun NavGraphCompany(
    navController: NavHostController,
    studentViewModel: StudentViewModel,
    internshipViewModel: InternshipViewModel,
    companyViewModel: CompanyViewModel,
    modifier: Modifier = Modifier,
    sharedDataViewModel: SharedDataViewModel,
    loginViewModel: LoginViewModel
){
    NavHost(
        navController = navController,
        startDestination = BottonNavItemCompany.InternshipApplicationsList.route,
        modifier = modifier
    ){
        // Pantalla de lista de solitudes
        composable(BottonNavItemCompany.InternshipApplicationsList.route) {
            InternshipApplicationsListScreen( navController = navController,
                studentViewModel = studentViewModel,
                companyViewModel = companyViewModel,
                sharedDataViewModel = sharedDataViewModel
            )
        }
        // Pantalla de lista de solitudes
        composable(BottonNavItemCompany.RegistarInterview.route) {
            RegisterInternshipScreen( navController = navController,
                companyViewModel = companyViewModel,
                sharedDataViewModel = sharedDataViewModel
            )

        }
        // Pantalla de lista de solitudes
        composable(BottonNavItemCompany.Interviews.route) {
            InternshipListScreen(navController = navController,
                internshipViewModel = internshipViewModel,
                companyViewModel = companyViewModel,
                sharedDataViewModel = sharedDataViewModel)

        }
        // Pantalla de CompanyAccount
        composable(BottonNavItemCompany.CompanyAccount.route) {
            AccountScreen(navController = navController, companyViewModel = companyViewModel,
                internshipViewModel = internshipViewModel,
                sharedDataViewModel = sharedDataViewModel,
                loginViewModel = loginViewModel)
        }

        composable("editInternship/{internshipId}") { backStackEntry ->
            val internshipId = backStackEntry.arguments?.getString("internshipId")?.toIntOrNull()
            internshipId?.let {
            EditInternshipDetailScreen(
                internshipId = internshipId,
                companyViewModel = companyViewModel,
                internshipViewModel = internshipViewModel,
                sharedDataViewModel = sharedDataViewModel,
                navController = navController
            )
            } ?: run {
                // Manejar el caso en que internshipId es nulo o no válido
                Text("ID de pasantía no válido")
            }
        }

        composable(NavRoutes.AccountScreen.ROUTE) { backStackEntry ->

            AccountScreen(
                navController = navController,
                companyViewModel = companyViewModel,
                internshipViewModel = internshipViewModel,
                sharedDataViewModel = sharedDataViewModel,
                loginViewModel = loginViewModel
            )

        }


        composable(NavRoutes.CompanyProfileScreen.ROUTE,
            arguments = listOf(navArgument(NavRoutes.CompanyProfileScreen.ARG_TASK_ID){
                type = NavType.LongType
            })){ backStackEntry ->
            val companyId = backStackEntry.arguments?.getLong(NavRoutes.CompanyProfileScreen.ARG_TASK_ID)?: 0L
            CompanyProfileScreen(
                companyId = companyId,
                companyViewModel = companyViewModel,
                navController = navController,
                sharedDataViewModel = sharedDataViewModel,
                internshipViewModel = internshipViewModel
            )
        }

        composable(NavRoutes.InternshipApplicationDetail.ROUTE,
            arguments = listOf(navArgument(NavRoutes.InternshipApplicationDetail.ARG_TASK_ID) {
                type = NavType.LongType
            })
        ) { backStackEntry ->
            val applicationId = backStackEntry.arguments?.getLong(NavRoutes.InternshipApplicationDetail.ARG_TASK_ID)?: 0L
            InternshipApplicationDetailScreen(
                applicationId = applicationId,
                companyViewModel = companyViewModel,
                internshipViewModel = internshipViewModel,
                navController = navController,

            )

        }
    }


}

