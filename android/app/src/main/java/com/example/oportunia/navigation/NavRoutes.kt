package com.example.oportunia.navigation

sealed class NavRoutes {

    /* STUDENT NAVROUTES*/
    data object InternshipList : NavRoutes(){
        const val  ROUTE = "InternshipList"

    }

    data object CVAnalysis : NavRoutes(){
        const val ROUTE = "CVAnalysis"
    }


    data object MyInternships : NavRoutes(){
        const val ROUTE = "MyInternships"
    }

    data object Interviews : NavRoutes(){
        const val ROUTE = "Interviews"
    }

    data object InternshipDetail : NavRoutes() {
        const val ROUTE = "internshipDetail/{internshipId}"
        const val ARG_TASK_ID = "internshipId" // Changed to uppercase with underscores

        fun createRoute(internshipId: Long) = "internshipDetail/$internshipId"
    }

    data object InterviewDetail : NavRoutes() {
        const val ROUTE = "interviewDetail/{internshipId}"
        const val ARG_TASK_ID = "internshipId"  // Esto se refiere al internshipId para la pasantía

        fun createRoute(internshipId: Long) = "interviewDetail/$internshipId"
    }

    data object MyInternshipDetail : NavRoutes() {
        const val ROUTE = "myInternshipDetail/{internshipId}"
        const val ARG_TASK_ID = "internshipId"

        fun createRoute(internshipId: Long) = "myInternshipDetail/$internshipId"
    }


    data object StudentProfile : NavRoutes(){
        const val ROUTE = "studentProfile/"
    }

    data object CVanalysisdetail : NavRoutes(){
        const val ROUTE = "CVanalysisdetail"
    }

    data object  StudentProfileDetail: NavRoutes(){
        const val ROUTE = "studentProfileDetail/{studentId}"
        const val ARG_TASK_ID = "studentId"

        fun createRoute(studentId: Long) = "studentProfileDetail/$studentId"
    }

    //faltan los de company

    /* COMPANY NAVROUTES*/

    data object InternshipApplicationDetail : NavRoutes() {
        const val ROUTE = "internshiAplicationDetail/{applicationId}"
        const val ARG_TASK_ID = "applicationId"

        fun createRoute(applicationId: Long) = "internshiAplicationDetail/$applicationId"
    }

    data object AccountScreen : NavRoutes() {
        const val ROUTE = "accountScreen/"
    }

    data object CompanyProfileScreen : NavRoutes() {
        const val ROUTE = "companyProfileScreen/{companyId}"
        const val ARG_TASK_ID = "companyId"

        fun createRoute(companyId: Long) = "companyProfileScreen/$companyId"
    }

}