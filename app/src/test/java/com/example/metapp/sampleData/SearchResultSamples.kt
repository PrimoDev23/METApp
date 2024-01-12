package com.example.metapp.sampleData

import com.example.metapp.data.models.SearchResponse
import com.example.metapp.domain.models.SearchResult

object SearchResultSamples {

    val empty = SearchResult(ids = emptyList())

    val filledResponse = SearchResponse(
        total = 4,
        objectIds = listOf(0, 1, 2, 3)
    )

    val filled = SearchResult(ids = listOf(0, 1, 2, 3))

}