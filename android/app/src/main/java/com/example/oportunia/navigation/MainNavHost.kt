package com.example.oportunia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.StudentViewModel

@Composable
fun MainNavHost(
    navController: NavHostController,
    userRole: String,
    internshipViewModel: InternshipViewModel,
    studentViewModel: StudentViewModel,
    companyViewModel: CompanyViewModel,
    sharedDataViewModel: SharedDataViewModel,
    loginViewModel: LoginViewModel,
    authPreferences: AuthPreferences
) {
    if (userRole == "ROLE_STUDENT") {
        NavGraph(
            navController = navController,
            internshipViewModel = internshipViewModel,
            sharedDataViewModel = sharedDataViewModel,
            studentViewModel = studentViewModel,
            loginViewModel = loginViewModel,
            authPreferences = authPreferences
        )
    } else if (userRole == "ROLE_COMPANY") {
        NavGraphCompany(
            navController = navController,
            internshipViewModel = internshipViewModel,
            studentViewModel = studentViewModel,
            companyViewModel = companyViewModel,
            sharedDataViewModel = sharedDataViewModel,
            loginViewModel = loginViewModel
        )
    }
}
