package com.nt.moviesapp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.nt.moviesapp.R
import com.nt.moviesapp.databinding.ActivityMainBinding
import com.nt.moviesapp.repo.MovieRepository
import com.nt.moviesapp.viewmodel.MovieViewModel
import com.nt.moviesapp.viewmodel.MovieViewModelProviderFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var movieRepository: MovieRepository
    private lateinit var viewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = MovieViewModelProviderFactory(application, movieRepository)
        viewModel = ViewModelProvider(this, factory).get(MovieViewModel::class.java)

    }
}