package com.example.metapp.sampleData

import com.example.metapp.domain.models.SearchResult

object SearchResultSamples {

    val empty = SearchResult(ids = emptyList())

    val filled = SearchResult(ids = listOf(0, 1, 2, 3))

}