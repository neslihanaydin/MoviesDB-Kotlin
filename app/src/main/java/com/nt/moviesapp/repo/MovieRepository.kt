package com.nt.moviesapp.repo

import com.nt.moviesapp.api.RetrofitAPI
import com.nt.moviesapp.model.Movie
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val retrofitAPI: RetrofitAPI
) : MovieRepositoryInterface {

    override suspend fun searchMovie(movieTitle: String, pageNumber: Int) =
        retrofitAPI.movieSearch(movieTitle, pageNumber)

    override suspend fun getMovieDetails(imdbID: String): Movie {
        return retrofitAPI.getMovie(imdbID)
    }
}