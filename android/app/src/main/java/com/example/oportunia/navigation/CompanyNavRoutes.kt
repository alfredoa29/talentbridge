package com.example.oportunia.navigation

//debo de hacer un refactor a navgrapcompany con esto
sealed class CompanyNavRoutes {
    data object InternshipApplicationsList : CompanyNavRoutes() {
        const val ROUTE = "internshipApplicationsList"
    }

    data object RegisterInternship : CompanyNavRoutes() {
        const val ROUTE = "registerInternship"
    }

    data object Interviews : CompanyNavRoutes() {
        const val ROUTE = "interviews"
    }

    data object CompanyAccount : CompanyNavRoutes() {
        const val ROUTE = "companyAccount"
    }

    data object EditInternship : CompanyNavRoutes() {
        const val ROUTE = "editInternship/{internshipId}"
        const val ARG_INTERNSHIP_ID = "internshipId"

        fun createRoute(internshipId: Long) = "editInternship/$internshipId"
    }

    data object CompanyProfile : CompanyNavRoutes() {
        const val ROUTE = "companyProfile"
    }

    data object InternshipApplicationDetail : CompanyNavRoutes() {
        const val ROUTE = "internshipApplicationDetail/{internshipId}"
        const val ARG_INTERNSHIP_ID = "internshipId"

        fun createRoute(internshipId: Long) = "internshipApplicationDetail/$internshipId"
    }

    data object CompanyProfileScreen : NavRoutes() {
        const val ROUTE = "companyProfileScreen/{companyId}"
        const val ARG_TASK_ID = "companyId"

        fun createRoute(companyId: Long) = "companyProfileScreen/$companyId"
    }
}