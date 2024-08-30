package com.nt.moviesapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nt.moviesapp.MoviesApplication
import com.nt.moviesapp.model.Movie
import com.nt.moviesapp.model.SearchResponse
import com.nt.moviesapp.repo.MovieRepositoryInterface
import com.nt.moviesapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject
@HiltViewModel
class MovieViewModel @Inject constructor(
    application: Application,
    private val repository: MovieRepositoryInterface
) : AndroidViewModel(application) {
    val searchMoviesLiveData: MutableLiveData<Resource<MutableList<Movie>>> = MutableLiveData()

    var searchMoviesPage = 1
    var searchMoviesResponse: SearchResponse? = null // managing results with pagination

    var selectedMovie: MutableLiveData<Movie> = MutableLiveData()

    // Search movies by title. If shouldPaginate is true, then it will fetch next page of results
    fun searchMovies(movieTitle: String, shouldPaginate: Boolean) = viewModelScope.launch {
        safeSearchMoviesCall(movieTitle, shouldPaginate)
    }

    private suspend fun safeSearchMoviesCall(movieTitle: String, shouldPaginate: Boolean) {
       searchMoviesLiveData.postValue(Resource.Loading())
        try {
            if (shouldPaginate) {
                searchMoviesPage++
            } else {
                // if shouldPaginate is false, that means we are searching for a new movie
                searchMoviesPage = 1
            }
                val response = repository.searchMovie(movieTitle, searchMoviesPage)
               // Log.d("MovieViewModel", "Response: $response")
                searchMoviesLiveData.postValue(handleSearchMoviesResponse(response, shouldPaginate))
        } catch (t: Throwable) {
            when(t) {
                is IOException -> searchMoviesLiveData.postValue(Resource.Error("Network Failure"))
                else -> searchMoviesLiveData.postValue(Resource.Error("Conversion Error ${t.message}"))
            }
        }
    }

    private fun handleSearchMoviesResponse(response: Response<SearchResponse>, shouldPaginate: Boolean): Resource<MutableList<Movie>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                var movies = resultResponse.Search
                if (shouldPaginate) {
                    val oldResults = searchMoviesResponse?.Search
                    val newResults = resultResponse.Search
                    if (oldResults != null) {
                        oldResults.addAll(newResults)
                        movies = oldResults
                    }
                }
                if (searchMoviesResponse == null || !shouldPaginate) {
                    searchMoviesResponse = resultResponse
                } else {
                    searchMoviesResponse?.Search = movies
                }
                // if movies is null, then return an empty list. User might have searched for a movie that doesn't exist
                return Resource.Success(movies ?: mutableListOf())
            }
        }
        return Resource.Error(response.message())
    }

    suspend fun getMovieDetails(imdbId: String){
        val response = repository.getMovieDetails(imdbId)
        Log.d("MOVIE", response.Plot)
        selectedMovie.postValue(response)
       // return response
    }
}