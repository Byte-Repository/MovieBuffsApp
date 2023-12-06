package com.example.moviebuffsapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviebuffsapp.network.MovieApi
import com.example.moviebuffsapp.network.Movies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MovieUiState {
    data class Success(val movies: List<Movies>) : MovieUiState
    object Error : MovieUiState
    object Loading : MovieUiState
}

data class UiState(
    val currentMovie: Movies?,
    val movies: List<Movies>,
    val isShowingListPage: Boolean = true,
)

class MovieViewModel : ViewModel() {
    // The mutable State that stores the status of the most recent request
    var movieUiState: MovieUiState by mutableStateOf(MovieUiState.Loading)
        private set

    // New mutable State for navigation
    private val _uiState = MutableStateFlow(
        UiState(
            currentMovie = null,
            movies = listOf(
                Movies("Title1", "Poster1", "Description1", "releaseDate", "contentRating", "reviewScore", "bigImage", "length")
            ),
            isShowingListPage = true
        )
    )

    val uiState: StateFlow<UiState> = _uiState

    /**
     * Call getMovies() on init so we can display status immediately.
     */
    init {
        getMovies()
    }

    fun updateCurrentMovie(selectedMovie: Movies) {
        _uiState.update {
            it.copy(currentMovie = selectedMovie)
        }
    }

    fun navigateToListPage() {
        _uiState.update {
            it.copy(isShowingListPage = true)
        }
    }

    fun navigateToDetailPage() {
        _uiState.update {
            it.copy(isShowingListPage = false)
        }
    }


    /**
     * Gets Movies information from the Movie API Retrofit service and updates
     * [Movie] [List] [MutableList].
     */
    fun getMovies() {
        viewModelScope.launch {
            movieUiState = try {
                MovieUiState.Success(MovieApi.retrofitService.getMovies())
            } catch (e: IOException) {
                MovieUiState.Error
            }
        }
    }
}
