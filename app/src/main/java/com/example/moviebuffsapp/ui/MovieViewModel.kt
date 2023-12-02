package com.example.moviebuffsapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffsapp.network.MovieApi
import com.example.moviebuffsapp.network.MoviePhoto
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieUiState {
    data class Success(val photos: List<MoviePhoto>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

class MovieViewModel : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    /**
     * Call getMoviePhotos() on init so we can display status immediately.
     */
    init {
        getMoviePhotos()
    }

    /**
     * Gets Movie photos information from the Movie API Retrofit service and updates
     * [MoviePhoto] [List] [MutableList].
     */
    fun getMoviePhotos() {
        viewModelScope.launch {
            movieUiState = try {
                MovieUiState.Success(MovieApi.retrofitService.getPhotos())
            } catch (e: IOException) {
                MovieUiState.Error
            }
        }
    }
}