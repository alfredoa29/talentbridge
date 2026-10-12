package com.example.oportunia.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.oportunia.navigation.BottonNavItemStudent
import com.example.oportunia.presentation.ui.theme.red810
import com.example.oportunia.presentation.ui.theme.whiteTitle
import com.example.oportunia.presentation.viewmodel.InternshipViewModel

@Composable
fun BottonNavigationBarStudent(navController: NavController, internshipViewModel: InternshipViewModel){
    val  items = listOf(
        BottonNavItemStudent.InternshipList,
        BottonNavItemStudent.CVAnalysis,
        BottonNavItemStudent.MyInternships,
        BottonNavItemStudent.Interviews
    )
    val selectedIcons = listOf(Icons.Filled.Home, Icons.Filled.Favorite, Icons.Filled.Star)

    val selectedItem = remember { mutableStateOf(items[0]) }

    NavigationBar(containerColor = red810) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach {  item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.title)
                    )
                },
                label = { Text(text = stringResource(id = item.title)) },
                selected = currentRoute == item.route,
                onClick = {
                    when (item) {
                        is BottonNavItemStudent.InternshipList -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottonNavItemStudent.CVAnalysis -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottonNavItemStudent.MyInternships -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                        is BottonNavItemStudent.Interviews -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = whiteTitle,
                    selectedIconColor = red810,
                )
            )
        }


    }

}