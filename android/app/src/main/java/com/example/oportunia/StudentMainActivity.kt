package com.example.oportunia

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.oportunia.data.local.AuthPreferences
import com.example.oportunia.navigation.NavGraph
import com.example.oportunia.presentation.ui.components.BottonNavigationBarStudent
import com.example.oportunia.presentation.ui.theme.OportuniaTheme
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.StudentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StudentMainActivity : ComponentActivity() {

    private val internshipViewModel: InternshipViewModel by viewModels()
   // private val interviewViewModel: InterviewViewModel by viewModels()
   private val studentViewModel: StudentViewModel by viewModels()
    private val sharedDataViewModel: SharedDataViewModel by viewModels()
    @Inject
    lateinit var authPreferences: AuthPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // internshipViewModel.findAllInternships()
      //  interviewViewModel.findAllInterviews()
        // Cargar userid   studentId cuando inicie la actividad

        lifecycleScope.launch {
            try {
                internshipViewModel.findAllInternships()
                val username = authPreferences.getUsername()
                if (username != null) {
                    val studentId = studentViewModel.getStudentIdByUsername(username)
                    Log.d("StudentMainActivity", "Login successful for student: $username")
                    if (username != null) {
                        sharedDataViewModel.setuser(studentId)
                    }
                }
            } catch (e: Exception) {
                Log.e("StudentMainActivity", "Error al obtener studentId", e)
                // Aquí podrías mostrar un mensaje al usuario, o redirigir al login
            }
        }


        setContent {
            val navController = rememberNavController()

            OportuniaTheme {
                androidx.compose.material3.Scaffold(
                    bottomBar = {

                        BottonNavigationBarStudent(navController, internshipViewModel)

                    }
                ) { innerPadding ->

                    /*
                    // Navigate to the NavGraph when login is successful
                    NavGraph(
                        navController = navController,
                        internshipViewModel = internshipViewModel,
                        //    interviewViewModel = interviewViewModel,
                        sharedDataViewModel = sharedDataViewModel,
                        studentViewModel = studentViewModel,
                        loginViewModel = loginViewModel,


                        modifier = Modifier.padding(innerPadding)
                    )


                     */
                }
            }
        }
    }
}
