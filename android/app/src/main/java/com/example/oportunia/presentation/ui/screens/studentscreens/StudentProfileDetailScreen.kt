package com.example.oportunia.presentation.ui.screens.studentscreens

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
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.runtime.derivedStateOf
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
import com.example.oportunia.data.remote.dto.StudentUpdateDto
import com.example.oportunia.domain.model.Country
import com.example.oportunia.domain.model.Gender
import com.example.oportunia.presentation.ui.components.GenericDropdown
import com.example.oportunia.presentation.ui.layout.DetailsLayout
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.buttonBrownLine
import com.example.oportunia.presentation.ui.theme.cardCVColor
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel
import com.example.oportunia.viewmodel.StudentViewModel
import com.example.oportunia.viewmodel.UiEvent
import kotlinx.coroutines.launch
import java.io.File


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun StudentProfileDetailScreen( studentId: Long,
                                studentViewModel: StudentViewModel,
                                sharedDataViewModel: SharedDataViewModel,
                                navController: NavController){

    val isLoading by studentViewModel.isLoading.collectAsState()

    val selectedStudent by studentViewModel.selectedStudent.collectAsState()

    var fullname by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    //var country by remember { mutableStateOf("") }
    var university by remember { mutableStateOf("") }
    var career by remember  { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    var isSubmitted by remember { mutableStateOf(false) }

    var isInitialized by remember { mutableStateOf(false) }

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imageToDisplay = selectedImageUri ?: selectedStudent?.profilePicUrl?.let { Uri.parse(it) }




    val genderList by sharedDataViewModel.genderList.collectAsState()
    var selectedGender by remember { mutableStateOf<Gender?>(null) }
    val genderNames = genderList.map { it.name }
    var selectedGenderId by remember { mutableStateOf<Long?>(null) }
    var selectedGenderName by remember { mutableStateOf("") }

    val countryList by sharedDataViewModel.countryList.collectAsState()
    var selectedCountry by remember { mutableStateOf<Country?>(null) }
    val countryNames = countryList.map { it.name }
    var selectedCountryId by remember { mutableStateOf<Long?>(null) }
    var selectedCountryName by remember { mutableStateOf("") }


    val context = LocalContext.current

    //list of skills that comes from the backend
    val skillList by sharedDataViewModel.skillList.collectAsState()
    // skills that the student already has
    val studentSkills = selectedStudent?.skillList ?: emptyList()



// Usamos una lista mutable con IDs para seguimiento eficiente
    val selectedSkillIds = remember {
        mutableStateListOf<Long>().apply {
            addAll(studentSkills.map { it.id })
        }
    }

    // Para mostrar, mapeamos IDs a nombres
    val selectedSkillNames = skillList
        .filter { selectedSkillIds.contains(it.id) }
        .map { it.name }
        .toMutableStateList()

    //esto para areas de interes

    val categoryList by sharedDataViewModel.categoryList.collectAsState()
    // areas-categories that the student already has
    val studentCategories = selectedStudent?.areaOfInterestList ?: emptyList()


    // Usamos una lista mutable con IDs para seguimiento eficiente
    val selectedCategoriesIds = remember {
        mutableStateListOf<Long>().apply {
            addAll(studentCategories.map { it.id })
        }
    }

    // Para mostrar, mapeamos IDs a nombres
    val selectedCategoriesNames = categoryList
        .filter { selectedCategoriesIds.contains(it.id) }
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
        studentViewModel.uiEvent.collect { event: UiEvent ->
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
        sharedDataViewModel.getAllGenders()
        sharedDataViewModel.getAllCountries()
        sharedDataViewModel.getAllSkills()
        sharedDataViewModel.getAllCategories()
    }

    val studentId by sharedDataViewModel.currentUser


    LaunchedEffect(studentId) {
        studentId?.let {
            studentViewModel.selectStudentById(it)
        }
    }

    LaunchedEffect(selectedStudent?.id) {
        val student = selectedStudent
        if (student != null && !isInitialized) {
            fullname = student.fullName ?: ""
            username = student.username ?: ""
            email = student.email ?: ""
            university = student.university ?: ""
            career = student.career ?: ""

            student.gender?.let { gender ->
                selectedGender = gender
                selectedGenderName = gender.name ?: ""
                selectedGenderId = gender.id
            }

            student.country?.let { country ->
                selectedCountry = country
                selectedCountryName = country.name ?: ""
                selectedCountryId = country.id
            }

            isInitialized = true
        }
    }




    val isFormValid = fullname.isNotBlank() &&
            username.isNotBlank() &&
            email.isNotBlank() &&
            career.isNotBlank() &&
            university.isNotBlank()

        //    && selectedCategoryId != null

    DetailsLayout(
        navController = navController,
        topBarTitle = stringResource(R.string.student_profile_details),
        snackbarHostState = snackbarHostState
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

                            value = fullname,
                            onValueChange = { fullname = it },
                            label = {
                                Text(
                                    "Full name",
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

                            value = username,
                            onValueChange = { username = it },
                            label = {
                                Text(
                                    "Username",
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
                            value = email,
                            onValueChange = { email = it },
                            label = {
                                Text(
                                    "Email",
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
                            value = university,
                            onValueChange = { university = it },
                            label = {
                                Text(
                                    "University",
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
                            value = career,
                            onValueChange = { career = it },
                            label = {
                                Text(
                                    "Career",
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

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp), // Espacio entre los dropdowns
                            modifier = Modifier.fillMaxWidth()
                        ){
                            GenericDropdown( //composable that is in components
                                label = "Gender",
                                options = genderNames,
                                selectedOption = selectedGenderName,
                                onOptionSelected = { name ->
                                    selectedGenderName = name
                                    val selected = genderList.find { it.name == name }
                                    selectedGenderId = selected?.id
                                },
                                modifier = Modifier.weight(1f)


                            )

                            GenericDropdown(
                                label = "País",
                                options = countryNames,
                                selectedOption = selectedCountryName,
                                onOptionSelected = { name ->
                                    selectedCountryName = name
                                    selectedCountryId = countryList.find { it.name == name }?.id
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        MultiSelectDropdown(
                            label = "Skills",
                            options = skillList.map { it.name },
                            selectedOptions = selectedSkillNames,
                            onOptionToggled = { optionName ->
                                val skill = skillList.find { it.name == optionName }
                                skill?.let {
                                    if (selectedSkillIds.contains(it.id)) {
                                        selectedSkillIds.remove(it.id)
                                        selectedSkillNames.remove(optionName)
                                    } else {
                                        selectedSkillIds.add(it.id)
                                        selectedSkillNames.add(optionName)
                                    }
                                }
                            }
                        )

                        MultiSelectDropdown(
                            label = "Areas Of interest",
                            options = categoryList.map { it.name },
                            selectedOptions = selectedCategoriesNames,
                            onOptionToggled = { optionName ->
                                val categories = categoryList.find { it.name == optionName }
                                categories?.let {
                                    if (selectedCategoriesIds.contains(it.id)) {
                                        selectedCategoriesIds.remove(it.id)
                                        selectedCategoriesNames.remove(optionName)
                                    } else {
                                        selectedCategoriesIds.add(it.id)
                                        selectedCategoriesNames.add(optionName)
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
                    text = { Text("¿Deseas confirmar la actualizacion de la pasantía?") },
                    confirmButton = {
                        Button(
                            onClick = {
                                showDialog = false

                                val updateDto = StudentUpdateDto(
                                    id = selectedStudent!!.id,
                                    username = username,
                                    email = email,
                                    university = university,
                                    career = career,
                                    fullName = fullname,
                                    genderId = selectedGenderId!!,
                                    countryId = selectedCountryId!!,
                                    skillIds = selectedSkillIds.toList(),
                                    areaOfInterestIds = selectedCategoriesIds.toList()

                                )
                                Log.d("updateDto", "Valor de updateDto: $updateDto")
                                studentViewModel.updateStudent(updateDto,  selectedImageUri?.let { convertUriToFile(context, it) })
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



