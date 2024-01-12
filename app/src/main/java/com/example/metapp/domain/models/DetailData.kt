package com.example.metapp.domain.models

import androidx.compose.runtime.Immutable

@Immutable
data class DetailData(
    val primaryImage: String,
    val additionalImages: List<String>,
    val isHighlight: Boolean,
    val title: String,
    val department: String,
    val objectUrl: String
)
