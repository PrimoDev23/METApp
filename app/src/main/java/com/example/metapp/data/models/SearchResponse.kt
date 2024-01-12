package com.example.metapp.data.models

import com.example.metapp.domain.models.SearchResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val total: Int,
    @SerialName("objectIDs")
    val objectIds: List<Int>?
) {
    fun toDomainModel() = SearchResult(
        ids = objectIds.orEmpty()
    )
}
