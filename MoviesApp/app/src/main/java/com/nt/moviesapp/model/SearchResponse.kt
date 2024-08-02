package com.nt.moviesapp.model

data class SearchResponse(
    val Response: String,
    var Search: MutableList<Movie>,
    val totalResults: String
)