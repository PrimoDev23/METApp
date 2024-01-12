package com.example.metapp.data.models

import com.example.metapp.domain.models.DetailData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailResponse(
    val primaryImage: String,
    val additionalImages: List<String>,
    val isHighlight: Boolean,
    val title: String,
    val department: String,
    @SerialName("objectURL")
    val objectUrl: String
) {
    fun toDomainModel() = DetailData(
        primaryImage = primaryImage,
        additionalImages = additionalImages,
        isHighlight = isHighlight,
        title = title,
        department = department,
        objectUrl = objectUrl
    )
}