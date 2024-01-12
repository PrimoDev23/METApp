package com.example.metapp.sampleData

import com.example.metapp.domain.models.DetailData

object DetailDataSamples {

    val detailData = DetailData(
        primaryImage = "",
        additionalImages = emptyList(),
        isHighlight = true,
        title = "Mona Lisa",
        department = "Art",
        objectUrl = "https://www.google.com"
    )

}