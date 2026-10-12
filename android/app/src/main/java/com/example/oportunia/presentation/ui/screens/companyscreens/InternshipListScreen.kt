package com.example.oportunia.presentation.ui.screens.companyscreens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.domain.model.Internship
import com.example.oportunia.presentation.ui.components.InternShipItemCompany
import com.example.oportunia.presentation.ui.layout.MainLayoutCompany
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel


@Composable
fun InternshipListScreen(
    navController: NavController,
    internshipViewModel: InternshipViewModel,
    companyViewModel: CompanyViewModel,
    sharedDataViewModel: SharedDataViewModel,

    ) {



    val currentUser by sharedDataViewModel.currentUser

    LaunchedEffect(currentUser) {
        currentUser?.let {
            companyViewModel.findAllInternshipsByCompanyId(it)
        }
    }

    //val isLoading by internshipViewModel.isLoading.collectAsState()
    val internshipList by companyViewModel.internshipList.collectAsState()



    fun toggleStatus(updatedInternship: Internship) {
        val internshipInputDto = InternshipInputDto(
            id = updatedInternship.id,
            active = updatedInternship.active
        )
        currentUser?.let { user ->
            companyViewModel.updateInternship(internshipInputDto, user)
        } ?: run {
            Log.w("toggleStatus", "Usuario no disponible para actualizar")
        }
    }



    MainLayoutCompany(navController = navController,
        topBarTitle = stringResource(R.string.intership_title),

    ) { paddingValues ->
        if (internshipList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .padding(paddingValues),  //aplicar este padding que creo que venia desde el main

                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hay pasantías disponibles.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 0.dp)
                    .padding(paddingValues) //aplicar este padding que creo que venia desde el main
                //pading de las tarjetas de listas de internship list screen

            ) {

                items(internshipList) { internship ->
                    InternShipItemCompany(internship = internship, onClick = {
                        navController.navigate("editInternship/${internship.id}")
                    },  onToggleStatus = { toggleStatus(it) })
                }
            }
        }
/*
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

 */
    }
}

