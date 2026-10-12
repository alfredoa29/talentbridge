package com.example.oportunia.presentation.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.ui.theme.whiteTitle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(navController: NavController, topBarTitle: String,
               content: @Composable (PaddingValues) -> Unit) {

    var presses by remember { mutableIntStateOf(0) }

    Surface (modifier = Modifier
        .fillMaxSize()
        .padding(0.dp)
        .background(backgroundCream)

    ){
        Scaffold(

            containerColor = backgroundCream, //este es el que le da el blanco al fondo o el cream
            topBar = {
                //val topBarName = stringResource(id = R.string.intership_title)

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
                    actions = {
                        IconButton(onClick = { navController.navigate("studentProfile/")  }) {
                            Icon(
                                imageVector = Icons.Filled.AccountCircle,
                                contentDescription = "Localized description"
                            )
                        }
                    },

                )
            },

        ) { innerPadding ->
            content(innerPadding)
        }
    }
}