package com.nt.moviesapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nt.moviesapp.MoviesApplication
import com.nt.moviesapp.R
import com.nt.moviesapp.adapters.MoviesAdapter
import com.nt.moviesapp.databinding.FragmentDetailsMovieBinding
import com.nt.moviesapp.databinding.FragmentMovieBinding
import com.nt.moviesapp.repo.MovieRepository
import com.nt.moviesapp.util.Constants.QUERY_PAGE_SIZE
import com.nt.moviesapp.util.Constants.SEARCH_MOVIES_TIME_DELAY
import com.nt.moviesapp.util.Resource
import com.nt.moviesapp.viewmodel.MovieViewModel
import com.nt.moviesapp.viewmodel.MovieViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailsFragment : Fragment(R.layout.fragment_details_movie) {

    @Inject
    lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieViewModel
    private var fragmentBinding : FragmentDetailsMovieBinding? = null

    //val imdbID = "tt0372784"
    // val args: ConfirmationFragmentArgs by navArgs()
    private val args: MovieDetailsFragmentArgs by navArgs()
    //private var imdbID = arguments?.getString("selectedMovieId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsMovieBinding.bind(view)
        fragmentBinding = binding

        val factory = MovieViewModelProviderFactory(MoviesApplication(), movieRepository)
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        val imdbID = args.selectedMovieId
        MainScope().launch {
            viewModel.getMovieDetails(imdbID)
        }

        viewModel.selectedMovie.observe(viewLifecycleOwner, Observer { movie ->
            Log.d("MOVIE FRAGMENT", movie.Title)
            binding.movieName.text = movie.Title
            Glide.with(this)
                .load(movie.Poster)
                .into(binding.movieFullPoster)
            binding.movieGenre.text = movie.Genre
            binding.moviePlot.text = movie.Plot
            binding.movieRating.text = movie.imdbRating
            binding.movieReleaseDate.text = movie.Released


        })
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}