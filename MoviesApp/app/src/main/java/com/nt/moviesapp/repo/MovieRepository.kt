package com.nt.moviesapp.repo

import com.nt.moviesapp.api.RetrofitAPI
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val retrofitAPI: RetrofitAPI
) : MovieRepositoryInterface {

    override suspend fun searchMovie(movieTitle: String, pageNumber: Int) =
        retrofitAPI.movieSearch(movieTitle, pageNumber)
}