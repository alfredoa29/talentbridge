package com.example.oportunia.presentation.ui.model

data class InterviewSection(
    val title: String,
    val questions: List<String>,
    var isExpanded: Boolean = false
)
