package com.example.moviesapptask.models.response


import com.example.moviesapptask.ui.viewitem.MovieViewItem
import com.example.moviesapptask.utilities.Constants
import com.example.moviesapptask.utilities.builders.recyclerview.AbstractViewItem
import com.example.moviesapptask.utilities.builders.recyclerview.ViewItemRepresentable

data class MoviesResponse(
    val page: Int,
    var results: List<Result> = listOf(),
    val total_pages: Int,
    val total_results: Int
) {
    data class Result(
        val id: Int? = null,
        val original_title: String? = null,
        val overview: String? = null,
        val poster_path: String? = null,
        val release_date: String? = null,
        val title: String? = null,
        val vote_average: Double? = null,
        val vote_count: Int? = null
    )
}


class SetDataViewModel(
    data: MoviesResponse.Result
) : ViewItemRepresentable {
    override val viewItem: AbstractViewItem
        get() = MovieViewItem(
            this
        )
    var id = data.id
    var posterPath = Constants.imageURL + data.poster_path
    var originalTitle = data.original_title
    var overview = data.overview
    var voteAverage = data.vote_average
    var releaseDate = data.release_date
}