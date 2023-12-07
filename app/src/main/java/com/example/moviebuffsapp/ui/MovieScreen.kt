/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.moviebuffsapp.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebuffsapp.R
import com.example.moviebuffsapp.network.Movies
import com.example.moviebuffsapp.ui.theme.MovieBuffsAppTheme
import com.example.moviebuffsapp.ui.utils.MoviesContentType

@Composable
fun HomeScreen(
    viewModel: MovieViewModel,
    movieUiState: MovieUiState,
    windowSize: WindowWidthSizeClass,
    contentType: MoviesContentType,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        movieUiState is MovieUiState.Loading -> {
            LoadingScreen(modifier = modifier.fillMaxSize())
        }
        movieUiState is MovieUiState.Success -> {
            if (uiState.isShowingListPage) {
                when (contentType) {
                    MoviesContentType.LIST_AND_DETAIL -> {
                        MovieListAndDetails(
                            movies = uiState.movies,
                            onClick = {
                                viewModel.updateCurrentMovie(it)
                            },
                            selectedMovie = uiState.currentMovie ?: movieUiState.movies[0],
                            contentPadding = contentPadding,
                            modifier = Modifier.fillMaxWidth()
                        )
                        MovieList(
                            movies = movieUiState.movies,
                            onClick = {
                                viewModel.updateCurrentMovie(it)
                                viewModel.navigateToDetailPage()
                            },
                            contentPadding = contentPadding,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                    else -> {
                    }
                }
            } else {
                MovieDetails(
                    movie = uiState.currentMovie ?: movieUiState.movies[0],
                    onBackPressed = { viewModel.navigateToListPage() },
                    contentPadding = contentPadding
                )
            }
        }
        movieUiState is MovieUiState.Error -> {
            ErrorScreen(modifier = modifier.fillMaxSize())
        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun MovieBuffsApp(
    windowSize: WindowWidthSizeClass,
    modifier: Modifier = Modifier
) {
    val viewModel: MovieViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    val contentType: MoviesContentType
    when (windowSize) {
        WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
            contentType = MoviesContentType.LIST_ONLY
        }

        WindowWidthSizeClass.Expanded -> {
            contentType = MoviesContentType.LIST_AND_DETAIL
        }

        else -> {
            contentType = MoviesContentType.LIST_ONLY
        }
    }
    Scaffold(
        topBar = {
            MovieBuffsAppBar(
                isShowingListPage = uiState.isShowingListPage,
                onBackButtonClick = { viewModel.navigateToListPage() },
            )
        }
    ) { innerPadding ->
        HomeScreen(
            viewModel = viewModel,
            movieUiState = viewModel.movieUiState,
            windowSize = windowSize,
            contentType = contentType,
            modifier = modifier,
            contentPadding = innerPadding
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieBuffsAppBar(
    onBackButtonClick: () -> Unit,
    isShowingListPage: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text =
                if (!isShowingListPage) {
                    stringResource(R.string.app_name)
                } else {
                    stringResource(R.string.app_name)
                }
            )
        },
        navigationIcon = if (!isShowingListPage) {
            {
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        } else {
            { Box {} }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier,
    )
}

//////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun MovieCard(
    movie: Movies,
    onClick: (Movies) -> Unit,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .clickable { onClick(movie) }
            .padding(top = 8.dp)
            .height(180.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(movie.poster)
                    .crossfade(true)
                    .build(),
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .width(120.dp)
            )

            Column(
                modifier = Modifier
                    .padding(0.dp) // Added padding for the entire Column
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp)) // Spacer with height of 4.dp

                Text(
                    text = movie.description,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(end = 4.dp)
                )

                Spacer(modifier = Modifier.height(8.dp)) // Spacer with height of 8.dp

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(end = 2.dp)
                    )
                    Text(
                        text = movie.reviewScore,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun MovieList(
    movies: List<Movies>,
    onClick: (Movies) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier
            .padding(start = 16.dp,
                end = 16.dp)
            .fillMaxWidth(),
    ) {
        items(items = movies, key = { movie -> movie.title }) { movie ->
            MovieCard(
                movie = movie,
                onClick = onClick
            )
        }
    }
}

@Composable
fun MovieDetails(
    movie: Movies,
    onBackPressed: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    BackHandler {
        onBackPressed()
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding()
    ) {

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.bigImage)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = "PG-13 | ${movie.length}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = movie.reviewScore,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.description,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}

@Composable
fun MovieListAndDetails(
    movies: List<Movies>,
    onClick: (Movies) -> Unit,
    selectedMovie: Movies,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        MovieList(
            movies = movies,
            onClick = onClick,
            contentPadding = contentPadding,
            modifier = Modifier
                .weight(2f)
                .padding(
                    start = 16.dp,
                    end = 16.dp
                )
        )
        MovieDetails(
            movie = selectedMovie,
            onBackPressed = { },
            contentPadding = contentPadding,
            modifier = Modifier.weight(3f)
        )
    }
}

//////////////////////////////////////////////////////////////////////////////////////////////////

@Preview
@Composable
fun MovieCardPreview() {
    val movie = Movies(
        title = "Iron Man",
        poster = "https://example.com/poster.jpg",
        description = "Iron Man is the best.",
        releaseDate = "July 30, 2001",
        contentRating = "PG-13",
        reviewScore = "9.0",
        bigImage = "https://example.com/big_image.jpg",
        length = "150 min"
    )

    MovieBuffsAppTheme {
        MovieCard(movie = movie, onClick = {})
    }
}

@Preview
@Composable
fun MovieListPreview() {
    val movies = List(3) {
        Movies(
            title = "Movie $it",
            poster = "https://example.com/poster_$it.jpg",
            description = "Description $it",
            releaseDate = "Release Date $it",
            contentRating = "PG-13",
            reviewScore = "7.$it",
            bigImage = "https://example.com/big_image_$it.jpg",
            length = "170 min"
        )
    }

    MovieBuffsAppTheme {
        MovieList(movies = movies, onClick = {})
    }
}

@Preview
@Composable
fun MovieDetailsPreview() {
    val movie = Movies(
        title = "Iron Man",
        poster = "https://example.com/poster.jpg",
        description = "Iron Man is the best.",
        releaseDate = "July 30, 2001",
        contentRating = "PG-13",
        reviewScore = "9.0",
        bigImage = "https://example.com/big_image.jpg",
        length = "150 min"
    )

    MovieBuffsAppTheme {
        MovieDetails(
            movie = movie,
            onBackPressed = {},
            contentPadding = PaddingValues()
        )
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun MovieListAndDetailsPreview() {
    val movies = List(7) { index ->
        Movies(
            title = "Movie $index",
            poster = "https://example.com/poster_$index.jpg",
            description = "Description $index",
            releaseDate = "Release Date $index",
            contentRating = "PG",
            reviewScore = "4.$index",
            bigImage = "https://example.com/big_image_$index.jpg",
            length = "120 min"
        )
    }

    val selectedMovie = Movies(
        title = "Selected Movie",
        poster = "https://example.com/selected_movie_poster.jpg",
        description = "Description for Selected Movie",
        releaseDate = "Selected Release Date",
        contentRating = "PG-13",
        reviewScore = "4.5",
        bigImage = "https://example.com/selected_movie_big_image.jpg",
        length = "150 min"
    )

    MovieBuffsAppTheme {
        MovieListAndDetails(
            movies = movies,
            onClick = { },
            selectedMovie = selectedMovie,
            contentPadding = PaddingValues(),
            modifier = Modifier.fillMaxWidth(0.5f)
        )
    }
}




