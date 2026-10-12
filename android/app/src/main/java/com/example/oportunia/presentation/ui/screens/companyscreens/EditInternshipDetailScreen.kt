package com.example.oportunia.presentation.ui.screens.companyscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.data.remote.dto.InternshipInputDto
import com.example.oportunia.presentation.ui.components.CategoryAutocompleteDropdown
import com.example.oportunia.presentation.ui.layout.DetailsLayoutCompany
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.bottonEditBrownCompany
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.cardCVColor
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.viewmodel.CompanyInternshipState

import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.UiEvent
import kotlinx.coroutines.launch


@Composable
fun EditInternshipDetailScreen(internshipId: Int,
                               companyViewModel: CompanyViewModel,
                               internshipViewModel: InternshipViewModel,
                               sharedDataViewModel: SharedDataViewModel,
                               navController: NavController){

    // val isLoading = remember { mutableStateOf(false) }

    var title by remember { mutableStateOf("") }
    var shortDescription by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val categories by companyViewModel.categoryList.collectAsState()

    val categoryNames = categories.map { it.name }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }
    var selectedCategoryName by remember { mutableStateOf("") }

    //aqui esta guardado el selectec intenship que viene con el id y se vuelve a cargar en el launchedeffect
    val selectedInternship by internshipViewModel.selectedInternship.collectAsState()
    var isInitialized by remember { mutableStateOf(false) }


    var isSubmitted by remember { mutableStateOf(false) }


    val currentUser by sharedDataViewModel.currentUser

    LaunchedEffect(currentUser) {
        currentUser?.let {
            companyViewModel.selectCompanyById(it)
        }
    }
    val internshipState by companyViewModel.internship.collectAsState()

    //--
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        companyViewModel.uiEvent.collect { event: UiEvent ->
            when(event) {
                is UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(event.message)
                    }
                }

                else -> {}
            }
        }
    }

    /*
    LaunchedEffect(internshipState) {
        if (isSubmitted) {
            when (internshipState) {
                is CompanyInternshipState.Success -> {
                    snackbarHostState.showSnackbar("Pasantía actualizada exitosamente")
                    // Limpiar formulario solo si fue exitoso
                    title = ""
                    shortDescription = ""
                    description = ""
                    selectedCategoryId = null
                    selectedCategoryName = ""

                    // Volver a la pantalla anterior
                    navController.popBackStack()
                }
                is CompanyInternshipState.Error -> {
                    val errorMsg = (internshipState as CompanyInternshipState.Error).message
                    snackbarHostState.showSnackbar("Error al actualizar: $errorMsg")
                    isSubmitted = false
                }
                else -> { /* No hacer nada para Loading o Default */ }
            }
        }
    }


     */

//--

    val isFormValid = title.isNotBlank()
            && shortDescription.isNotBlank()
            && description.isNotBlank()
            && selectedCategoryId != null

    LaunchedEffect(internshipId) {
        isInitialized = false
        companyViewModel.getCategories()
        internshipViewModel.selectInternshipById(internshipId.toLong())

    }


    LaunchedEffect(selectedInternship?.id) {
        println("Loading internship: ${selectedInternship?.id}")
        if (selectedInternship != null && !isInitialized) {
            title = selectedInternship!!.title
            shortDescription = selectedInternship!!.shortDescription
            description = selectedInternship!!.description
            selectedCategoryId = selectedInternship!!.category.id
            selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name.orEmpty()
            isInitialized = true
        }
    }



    DetailsLayoutCompany(navController = navController,
        topBarTitle = stringResource(R.string.edit_internship),
        snackbarHostState = snackbarHostState,
        internshipViewModel = internshipViewModel
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Primera tarjeta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundCream),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(

                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Título",  fontWeight = FontWeight.Medium, color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Normal)

                    )
                    OutlinedTextField(
                        value = shortDescription,
                        onValueChange = { shortDescription = it },
                        label = { Text("Corta descripción",  fontWeight = FontWeight.Medium, color = Color.Gray) },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Normal)
                    )
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = {
                            Text(
                                "Descripción",
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 100.dp), // Altura mínima estilo párrafo
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.Normal
                        ),
                        maxLines = 10, // Puedes ajustar este valor
                        singleLine = false, // Para que se comporte como párrafo
                        placeholder = {
                            Text("Escribe una descripción detallada...")
                        }
                    )

                    CategoryAutocompleteDropdown(
                        categories = categoryNames,
                        selectedCategory = selectedCategoryName,
                        onCategorySelected = { name ->
                            selectedCategoryName = name
                            val selected = categories.find { it.name == name }
                            selectedCategoryId = selected?.id
                        }
                    )
                    ElevatedButton(
                        onClick = {
                            title = ""
                            shortDescription = ""
                            description = ""
                            selectedCategoryId = null
                            selectedCategoryName = ""
                        },
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        modifier = Modifier
                            .width(80.dp)
                            .height(50.dp)
                            .align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = backgroundCream,
                            contentColor = bottonEditBrownCompany
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text("Limpiar")
                    }
                }
            }

            // Segunda tarjeta
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = cardCVColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = { showDialog = true },
                        enabled = isFormValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, buttonBrownLine),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = red810,
                            contentColor = Color.White,
                            disabledContainerColor = red810.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("Guardar", fontSize = 20.sp)
                    }
                }
            }

            if (showDialog) {
                androidx.compose.material3.AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Confirmación") },
                    text = { Text("¿Deseas confirmar la actualizacion de la pasantía?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false
                                val dto = InternshipInputDto(
                                    id = internshipId.toLong(),
                                    title = title,
                                    shortDescription = shortDescription,
                                    description = description,
                                    active = true,
                                    categoryId = selectedCategoryId!!,
                                    companyId = currentUser
                                )
                                companyViewModel.updateInternship(dto, 4L)
                                isSubmitted = true

                                title = ""
                                shortDescription = ""
                                description = ""
                                selectedCategoryId = null
                                selectedCategoryName = ""
                            },
                        ) {
                            Text("Guardar")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancelar")
                        }
                    }
                )
            }
        }
    }
}


