package com.example.oportunia.presentation.ui.screens.companyscreens

import MultiSelectDropdown
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.oportunia.R
import com.example.oportunia.data.remote.dto.CompanyUpdateDto
import com.example.oportunia.presentation.ui.layout.DetailsLayoutCompany
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.cardCVColor
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.UiEvent
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CompanyProfileScreen(
    companyId: Long,
    companyViewModel: CompanyViewModel,
    navController: NavController,
    sharedDataViewModel : SharedDataViewModel,
    internshipViewModel: InternshipViewModel
) {
    val isLoading by companyViewModel.isLoading.collectAsState()

    val selectedCompany by companyViewModel.selectedCompany.collectAsState()

    var name by remember { mutableStateOf("") }
    var website by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    var isSubmitted by remember { mutableStateOf(false) }

    var isInitialized by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imageToDisplay = selectedImageUri ?: selectedCompany?.pictureUrl?.let { Uri.parse(it) }


    val context = LocalContext.current

    val companySectorList by sharedDataViewModel.sectorList.collectAsState()
    Log.d("companySectorList", "companySectorList: ${companySectorList}")
    val companySectors = selectedCompany?.sectorList ?: emptyList()

    val selectedSectorsIds = remember {
        mutableStateListOf<Long>().apply {
            addAll(companySectors.map { it.id })
        }
    }

    // Para mostrar, mapeamos IDs a nombres
    val selectedSectorNames = companySectorList
        .filter { selectedSectorsIds.contains(it.id) }
        .map { it.name }
        .toMutableStateList()

    //este es el pick launcher que abre y selecciona la imagen
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            val file = convertUriToFile(context, it)
            // Aquí puede usar el archivo, por ejemplo subirlo o guardarlo en el ViewModel
            Log.d("MiTag", "Archivo temporal creado: ${file?.absolutePath}")
        }
    }

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

    LaunchedEffect(Unit) {
        sharedDataViewModel.getAllSectors()

    }


    LaunchedEffect(companyId) {

        Log.d("MiTag", "Este es un mensaje de depuración ${companyId}")

        companyViewModel.selectCompanyById(companyId)

    }

    LaunchedEffect(selectedCompany?.id) {
        val company = selectedCompany
        if (company != null && !isInitialized) {
            name = company.name ?: ""
            website = company.website ?: ""

            isInitialized = true
        }
    }




    val isFormValid = name.isNotBlank() &&
            website.isNotBlank()

    //    && selectedCategoryId != null

    DetailsLayoutCompany(
        navController = navController,
        topBarTitle = stringResource(R.string.company_profile_details),
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
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
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

                        // Imagen de perfil (ícono por defecto)
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.Center
                        ) {
                            if (imageToDisplay != null) {
                                AsyncImage(
                                    model = imageToDisplay,
                                    contentDescription = "Profile Image",
                                    modifier = Modifier.matchParentSize(), // Asegura que llene el círculo
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Default Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                            Text("Seleccionar imagen")
                        }
                        OutlinedTextField(

                            value = name,
                            onValueChange = { name = it },
                            label = {
                                Text(
                                    "Name",
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )

                        )

                        OutlinedTextField(

                            value = website,
                            onValueChange = { website = it },
                            label = {
                                Text(
                                    "Website",
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            textStyle = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )

                        )
                        // Adaptar a sectores
                        MultiSelectDropdown(
                            label = "Sectores",
                            options = companySectorList.map { it.name },
                            selectedOptions = selectedSectorNames,
                            onOptionToggled = { optionName ->
                                val skill = companySectorList.find { it.name == optionName }
                                skill?.let {
                                    if (selectedSectorsIds.contains(it.id)) {
                                        selectedSectorsIds.remove(it.id)
                                        selectedSectorNames.remove(optionName)
                                    } else {
                                        selectedSectorsIds.add(it.id)
                                        selectedSectorNames.add(optionName)
                                    }
                                }
                            }
                        )
                    }
                }


                // Círculo de carga
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
                    text = { Text("¿Deseas confirmar la actualizacion?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false

                                val updateDto = CompanyUpdateDto(
                                    id = selectedCompany!!.id,
                                    name = name,
                                    website = website,
                                    sectorListIds = selectedSectorsIds.toList()

                                )
                                Log.d("updateDto", "Valor de CompanyUpdateDto : $updateDto")
                                companyViewModel.updateCompany(updateDto,  selectedImageUri?.let { com.example.oportunia.presentation.ui.screens.studentscreens.convertUriToFile(context, it) })
                                isSubmitted = true
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



fun convertUriToFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File.createTempFile("upload", ".jpg", context.cacheDir)
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
