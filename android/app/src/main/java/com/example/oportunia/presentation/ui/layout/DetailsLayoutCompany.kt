package com.example.oportunia.presentation.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.ui.theme.whiteTitle
import com.example.oportunia.presentation.viewmodel.InternshipViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsLayoutCompany(navController: NavController, topBarTitle: String,
                         internshipViewModel: InternshipViewModel,
                         snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
                         content: @Composable (PaddingValues) -> Unit) {


    // Usa Scaffold para agregar una TopAppBar con el botón de "Atrás"
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
            .background(backgroundCream)

    ) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState){
                    data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = red810,
                    contentColor = whiteTitle,
                    actionColor = backgroundCream
                )
            } },
            containerColor = backgroundCream,

            topBar = {
                //val topBarName = stringResource(id = R.string.intership_details)
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = red810,
                        titleContentColor = whiteTitle,
                    ),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = topBarTitle,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            internshipViewModel.clearSelectedInternship()
                            navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },

                )
            }
        ) { innerPadding ->
            // Contenido de la pantalla
            content(innerPadding)

        }
    }
}


