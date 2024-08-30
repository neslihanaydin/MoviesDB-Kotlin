package com.nt.moviesapp.repo

import com.nt.moviesapp.model.Movie
import com.nt.moviesapp.model.SearchResponse
import retrofit2.Response

interface MovieRepositoryInterface {

    suspend fun searchMovie(movieTitle: String, pageNumber: Int) : Response<SearchResponse>

    suspend fun getMovieDetails(imdbID: String) : Movie
}