package com.example.oportunia


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.navigation.NavGraph
import com.example.oportunia.navigation.NavGraphCompany
import com.example.oportunia.presentation.ui.components.BottoNavigationBarCompany
import com.example.oportunia.presentation.ui.components.BottonNavigationBarStudent
import com.example.oportunia.presentation.ui.theme.OportuniaTheme
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel

import com.example.oportunia.viewmodel.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class CompanyMainActivity : ComponentActivity() {

    private val internshipViewModel: InternshipViewModel by viewModels()
    private val studentViewModel: StudentViewModel by viewModels()
    private val companyViewModel: CompanyViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewModel by viewModels()
    @Inject
    lateinit var authPreferences: AuthPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

// Cargar el userid (en este caso companyid) cuando inicie la actividad
        Log.d("CompanyMainActivity", "PRUEBA COMPANYMAIN")
        lifecycleScope.launch {
            try {
                //internshipViewModel.findAllInternships()

                val username = authPreferences.getUsername()
                Log.d("CompanyMainActivity", "Login successful for company: $username")
                //sharedDataViewModel.setuser(4)
                if (username != null) {
                    val companyId = companyViewModel.getCompanyIdByUsername(username)
                    Log.d("CompanyMainActivity", "Login successful for company: $companyId")
                    if (companyId != null) {
                        sharedDataViewModel.setuser(companyId)
                   }
                }
            } catch (e: Exception) {
                Log.e("StudentMainActivity", "Error al obtener companyId", e)
                // Aquí podrías mostrar un mensaje al usuario, o redirigir al login
            }
        }



      //  internshipViewModel.findAllInternships()
       //interviewViewModel.findAllInterviews()

        /*
        setContent {
            val navController = rememberNavController()

            OportuniaTheme {
                Scaffold(
                    bottomBar = {
                        BottoNavigationBarCompany(navController)
                    }
                ) { innerPadding ->
                    NavGraphCompany(
                        navController = navController,
                        internshipViewModel = internshipViewModel,
                        studentViewModel = studentViewModel,
                        companyViewModel = companyViewModel,
                        modifier = Modifier.padding(innerPadding),
                        sharedDataViewModel = sharedDataViewModel,
                        loginViewModel = loginViewModel
                    )
                }
            }


        }

         */
    }
}
