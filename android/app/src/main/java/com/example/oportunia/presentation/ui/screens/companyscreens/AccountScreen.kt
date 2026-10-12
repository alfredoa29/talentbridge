package com.example.oportunia.presentation.ui.screens.companyscreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oportunia.R
import com.example.oportunia.presentation.ui.layout.DetailsLayout
import com.example.oportunia.presentation.ui.layout.MainLayoutCompany
import com.example.oportunia.presentation.ui.theme.backgroundCream
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.viewmodel.CompanyViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil3.compose.AsyncImage
import com.example.oportunia.domain.model.Company
import com.example.oportunia.navigation.NavRoutes
import com.example.oportunia.presentation.ui.screens.studentscreens.InfoCard
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.layout.ContentScale
import com.example.oportunia.presentation.ui.layout.DetailsLayoutCompany
import com.example.oportunia.presentation.viewmodel.InternshipViewModel
import com.example.oportunia.presentation.viewmodel.LoginViewModel
import com.example.oportunia.presentation.viewmodel.SharedDataViewModel

@Composable
fun AccountScreen(
    navController: NavController,
    companyViewModel: CompanyViewModel,
    internshipViewModel: InternshipViewModel,
    sharedDataViewModel: SharedDataViewModel,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel
) {
    val isLoading by companyViewModel.isLoading.collectAsState()
    val selectedCompany by companyViewModel.selectedCompany.collectAsState()

    val currentUser by sharedDataViewModel.currentUser

    LaunchedEffect(currentUser) {
        currentUser?.let {
            companyViewModel.selectCompanyById(it)
        }
    }

    DetailsLayoutCompany(
        navController = navController,
        topBarTitle = stringResource(R.string.account_title),
        internshipViewModel = internshipViewModel,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = backgroundCream),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CompanyProfileContent(company = selectedCompany, navController = navController,  loginViewModel = loginViewModel)
                    }
                }

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
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanyProfileContent(company: Company?, navController: NavController, loginViewModel: LoginViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (company == null) {
            Text("Cargando empresa...", modifier = Modifier.padding(16.dp))
            return
        }

        Spacer(modifier = Modifier.height(60.dp))

        Text(
            text = company.name ?: "Nombre no disponible",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            textAlign = TextAlign.Center,
            color = Color.Black
        )

        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = company.pictureUrl,
                contentDescription = "Company Logo",
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {  loginViewModel.logout()},
            colors = ButtonDefaults.buttonColors(containerColor = red810),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoCard(title = "Nombre de la empresa", value = company.name ?: "No disponible")
        InfoCard(title = "Sitio web", value = company.website ?: "No disponible")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Sectores:",
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray
        )
        FlowRow(

            modifier = Modifier.fillMaxWidth()
        ) {
            if (company.sectorList.isNotEmpty()) {
                company.sectorList.forEach { skill ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE0E0E0))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = skill.name, fontSize = 14.sp, color = Color.Black)
                    }
                }
            } else {
                Text("Sin sectores registrados", fontSize = 14.sp, color = Color.Gray)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(NavRoutes.CompanyProfileScreen.createRoute(company.id))
            },
            colors = ButtonDefaults.buttonColors(containerColor = red810),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Configuración",
                color = Color.White,
                fontSize = 20.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}


