package com.example.oportunia.data.datasource

import com.example.oportunia.domain.model.Internship

/**
 * /*
 * Esta es una clase temporal para simular la interacción con datos de pasantías.
 */
class InternshipProvider {
    companion object {
        // Método para encontrar una pasantía por su ID
        fun findInternshipById(id: Int): Internship? {
            return if (id in internshipList.indices) internshipList[id] else null
        }

        // Método para obtener todas las pasantías
        fun findAllInternships(): List<Internship> {
            return internshipList
        }

        // Lista de pasantías de prueba
        private val internshipList = listOf(
            Internship(
                id = 1,
                title = " Desarrollo de Software",
                shortDescription = "Desarrolla aplicaciones móviles y web. utilizando tecnologías modernas como Kotlin, Jetpack Compose y Firebase.",
                description = "Desarrolla aplicaciones móviles y web utilizando tecnologías modernas como Kotlin, Jetpack Compose y Firebase."
            ),
            Internship(
                id = 2,
                title = " Marketing Digital",
                shortDescription = "Aprende estrategias de marketing digital y gestión de redes sociales para empresas líderes.",
                description = "Aprende estrategias de marketing digital y gestión de redes sociales para empresas líderes."
            ),
            Internship(
                id = 3,
                title = " Diseño Gráfico",
                shortDescription = "Crea diseños creativos para campañas y branding utilizando herramientas como Adobe Photoshop.",
                description = "Crea diseños creativos para campañas publicitarias y branding utilizando herramientas como Adobe Photoshop e Illustrator."
            ),
            Internship(
                id = 4,
                title = "Recursos Humanos",
                shortDescription = "Participa en procesos de reclutamiento  y selección de personal para diversas empresas.",
                description = "Participa en procesos de reclutamiento y selección de personal para diversas empresas."
            ),
            Internship(
                id = 5,
                title = " Finanzas",
                shortDescription = "Colabora en el análisis financiero y la gestión de presupuestos para proyectos empresariales.",
                description = "Colabora en el análisis financiero y la gestión de presupuestos para proyectos empresariales."
            )
        )
    }
}

 */
