package com.example.oportunia.navigation

import android.icu.text.CaseMap.Title
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.oportunia.R

sealed class BottonNavItemStudent(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    /**
     * internshipList represents the navigation item for the task list screen.
     */
    data object InternshipList : BottonNavItemStudent(NavRoutes.InternshipList.ROUTE, R.string.internship_list, Icons.AutoMirrored.Filled.List)

    /**
     * TaskDetail represents the navigation item for the task detail screen.
     */
    data object CVAnalysis : BottonNavItemStudent(NavRoutes.CVAnalysis.ROUTE, R.string.cv_analysis, Icons.Filled.Assessment)

    /**
     * Settings represents the navigation item for the settings screen.
     */
    data object MyInternships : BottonNavItemStudent(NavRoutes.MyInternships.ROUTE, R.string.my_internships, Icons.Filled.Work)

    data object Interviews : BottonNavItemStudent(NavRoutes.Interviews.ROUTE, R.string.interviews, Icons.Filled.CalendarToday)


}