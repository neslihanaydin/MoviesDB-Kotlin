package com.nt.moviesapp.view

import android.os.Bundle
import android.view.View
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nt.moviesapp.MoviesApplication
import com.nt.moviesapp.R
import com.nt.moviesapp.adapters.MoviesAdapter
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
class MovieFragment : Fragment(R.layout.fragment_movie) {

    @Inject
    lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieViewModel
    lateinit var moviesAdapter: MoviesAdapter
    private var fragmentBinding : FragmentMovieBinding? = null

    // variables for scrolling feature
    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    // Scroll Listener for RecyclerView
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalCount = viewModel.searchMoviesResponse?.totalResults?.toInt() ?: 1
            val currentItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= currentItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = currentItemCount <= totalCount
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling
            if(shouldPaginate) {
                viewModel.searchMovies(fragmentBinding?.etSearch?.text.toString(), shouldPaginate)
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentMovieBinding.bind(view)
        fragmentBinding = binding

        val factory = MovieViewModelProviderFactory(MoviesApplication(), movieRepository)
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

        setupRecyclerView()

        // Listen for text changes in the search field with delay
        var job: Job? = null
        binding.etSearch.addTextChangedListener { searchText ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_MOVIES_TIME_DELAY)
                searchText?.let {
                    if (searchText.toString().isNotEmpty()) {
                        viewModel.searchMovies(searchText.toString(), shouldPaginate = false)
                    } else if (searchText.toString().isEmpty()) {
                        moviesAdapter.differ.submitList(listOf())
                    }
                }
            }
        }

        // Handle recycler view item click
        moviesAdapter.setOnItemClickListener {clickedMovie ->
            Toast.makeText(activity, "Clicked: ${clickedMovie.Title}", Toast.LENGTH_LONG).show()
            // navigate to the details fragment
            val bundle = Bundle().apply {
                putSerializable("selectedMovieId", clickedMovie.imdbID)
            }
            findNavController().navigate(
                R.id.action_movieFragment_to_movieDetailsFragment, bundle
            )
        }

        // Observe the LiveData in the ViewModel
        viewModel.searchMoviesLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { moviesResponse ->
                        moviesAdapter.differ.submitList(moviesResponse.toList())
                        val totalCount = viewModel.searchMoviesResponse?.totalResults?.toInt() ?: 1
                        val totalPages = totalCount / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchMoviesPage == totalPages
                        if (isLastPage) {
                            binding.rvMovies.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setupRecyclerView() {
        moviesAdapter = MoviesAdapter()
        fragmentBinding!!.rvMovies.apply {
            adapter = moviesAdapter
            layoutManager = GridLayoutManager(activity, 2)
            addOnScrollListener(this@MovieFragment.scrollListener)
        }
    }

    private fun hideProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        fragmentBinding?.progressBar?.visibility = View.VISIBLE
        isLoading = true
    }

    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }
}