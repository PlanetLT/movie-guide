package com.planet.movieguide.data.model
data class Result(
    val page: Int,
    val results: ArrayList<Movie>,
    val total_pages: Int,
    val total_results: Int,
)