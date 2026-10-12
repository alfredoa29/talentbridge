package com.example.oportunia.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.oportunia.R

sealed class  BottonNavItemCompany (
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
) {
    /**
     * internshipList represents the navigation item for the task list screen.
     */
    data object InternshipApplicationsList : BottonNavItemCompany("InternshipApplicationsList", R.string.internship_applications_nav, Icons.AutoMirrored.Filled.List)

    /**
     * TaskDetail represents the navigation item for the task detail screen.
     */
    data object RegistarInterview : BottonNavItemCompany("RegistarInterview", R.string.register_internship_nav, Icons.Filled.CalendarToday)

    /**
     * Settings represents the navigation item for the settings screen.
     */
    data object Interviews : BottonNavItemCompany("Interviews", R.string.company_internships_nav, Icons.Filled.Work)

    data object CompanyAccount : BottonNavItemCompany("CompanyAccount", R.string.company_account_nav, Icons.Filled.AccountCircle)


}