package com.nt.moviesapp.api

import com.nt.moviesapp.model.Movie
import com.nt.moviesapp.model.SearchResponse
import com.nt.moviesapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {

    @GET("/")
    suspend fun movieSearch(
        @Query("s")
        movieTitle : String,
        @Query("page")
        pageNumber: Int = 1,
        @Query("apikey")
        apiKey : String = API_KEY,
    ) : Response<SearchResponse>

    @GET("/")
    suspend fun getMovie(
        @Query("i")
        imdbID : String,
        @Query("apikey")
        apiKey : String = API_KEY,
        ) : Movie
}