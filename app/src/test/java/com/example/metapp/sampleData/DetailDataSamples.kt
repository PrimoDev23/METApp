package com.example.metapp.sampleData

import com.example.metapp.data.models.DetailResponse
import com.example.metapp.domain.models.DetailData

object DetailDataSamples {

    val detailResponse = DetailResponse(
        primaryImage = "",
        additionalImages = emptyList(),
        isHighlight = true,
        title = "Mona Lisa",
        department = "Art",
        objectUrl = "https://www.google.com"
    )

    val detailData = DetailData(
        primaryImage = "",
        additionalImages = emptyList(),
        isHighlight = true,
        title = "Mona Lisa",
        department = "Art",
        objectUrl = "https://www.google.com"
    )

}