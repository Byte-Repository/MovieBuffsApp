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
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviebuffsapp.R
import com.example.moviebuffsapp.network.MovieInfo
import com.example.moviebuffsapp.ui.theme.MovieBuffsAppTheme

@Composable
fun MovieBuffsApp(
    movieUiState: MovieUiState, modifier: Modifier = Modifier
) {
    when (movieUiState) {
        is MovieUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is MovieUiState.Success -> MovieList(movieUiState.movies, modifier)
        is MovieUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
    }
}

//    Scaffold(
//        topBar = {
//            MovieBuffsAppBar(
//                isShowingListPage = uiState.isShowingListPage,
//                onBackButtonClick = { viewModel.navigateToListPage() },
//            )
//        }
//) { innerPadding ->
//    // TODO: Add simple navigation with if/else conditional to show Details page
//    if (uiState.isShowingListPage) {
//        MovieList(
//            movies = uiState.movieList,
//            onClick = {
//                viewModel.updateCurrentMovie(it)
//                viewModel.navigateToDetailPage()
//            },
//            contentPadding = innerPadding,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(
//                    top = dimensionResource(R.dimen.padding_medium),
//                    start = dimensionResource(R.dimen.padding_medium),
//                    end = dimensionResource(R.dimen.padding_medium),
//                )
//            )
//        } else {
//            MovieDetails(
//                selectedMovie = uiState.currentMovie,
//                contentPadding = innerPadding,
//                onBackPressed = {
//                    viewModel.navigateToListPage()
//                }
//            )
//        }
//    }
//}
//
//@Composable
//private fun MovieDetails(
//    selectedMovie: Movie,
//    onBackPressed: () -> Unit,
//    contentPadding: PaddingValues,
//    modifier: Modifier = Modifier
//) {
//    // TODO: Add BackHandler
//    BackHandler {
//        onBackPressed()
//    }

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

@Composable
fun MovieList(movies: List<MovieInfo>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp), // Added verticalArrangement
    ) {
        items(items = movies, key = { movie -> movie.title }) { movie ->
            MovieCard(
                movie = movie,
            )
        }
    }
}

@Composable
fun MovieCard(movie: MovieInfo, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
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
                contentDescription = stringResource(R.string.movie_buffs_app),
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
                        modifier = Modifier.padding(end =2.dp)
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
fun MovieDetails(
    movie: MovieInfo,
    selectedMovie: MovieInfo,
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
            .padding(top = 56.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {
        // Image Composable
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.poster)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Details Section
        Column(modifier = Modifier.padding(8.dp)) {
            // Row 1
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

            // Row 2
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
                    text = "${movie.releaseDate}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Row 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(end = 2.dp)
                )
                Text(
                    text = "${movie.reviewScore}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Movie Description Text Composable
        Text(
            text = movie.description,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun MovieDetailsPreview() {
    val movie = MovieInfo(
        title = "Inception",
        poster = "https://example.com/poster_inception.jpg",
        description = "A mind-bending movie about dreams and reality.",
        releaseDate = "July 16, 2010",
        contentRating = "PG-13",
        reviewScore = "4.8",
        bigImage = "https://example.com/big_image_inception.jpg",
        length = "148 min"
    )

    MovieBuffsAppTheme(){
        MovieDetails(movie = movie)
    }
}

@Preview
@Composable
fun MovieCardPreview() {
    val movie = MovieInfo(
        title = "Inception",
        poster = "https://example.com/poster_inception.jpg",
        description = "A mind-bending movie about dreams and reality.",
        releaseDate = "July 16, 2010",
        contentRating = "PG-13",
        reviewScore = "4.8",
        bigImage = "https://example.com/big_image_inception.jpg",
        length = "148 min"
    )

    MovieBuffsAppTheme() {
        MovieCard(movie = movie)
    }
}

@Preview
@Composable
fun MovieListPreview() {
    val movies = List(3) {
        MovieInfo(
            title = "Movie Title $it",
            poster = "https://example.com/poster_$it.jpg",
            description = "Description for Movie $it",
            releaseDate = "Release Date $it",
            contentRating = "PG",
            reviewScore = "4.$it",
            bigImage = "https://example.com/big_image_$it.jpg",
            length = "120 min"
        )
    }

    MovieBuffsAppTheme() {
        MovieList(movies = movies)
    }
}

