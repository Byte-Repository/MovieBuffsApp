package com.example.moviebuffsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviebuffsapp.ui.MovieBuffsApp
import com.example.moviebuffsapp.ui.theme.MovieBuffsAppTheme
import com.example.moviebuffsapp.ui.MovieViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val movieViewModel = MovieViewModel()

        setContent {
            MovieBuffsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val windowSize = calculateWindowSizeClass(this)
                    MovieBuffsApp(
                        viewModel = movieViewModel,
                        windowSize = windowSize.widthSizeClass
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieBuffsAppCompactPreview() {
    MovieBuffsAppTheme {
        val viewModel = MovieViewModel() // Create an instance of MovieViewModel
        Surface {
            MovieBuffsApp(
                viewModel = viewModel, // Pass the viewModel instance
                windowSize = WindowWidthSizeClass.Compact
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun MovieBuffsAppMediumPreview() {
    MovieBuffsAppTheme {
        val viewModel = MovieViewModel() // Create an instance of MovieViewModel
        Surface {
            MovieBuffsApp(
                viewModel = viewModel, // Pass the viewModel instance
                windowSize = WindowWidthSizeClass.Medium
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun MovieBuffsAppExpandedPreview() {
    MovieBuffsAppTheme {
        val viewModel = MovieViewModel() // Create an instance of MovieViewModel
        Surface {
            MovieBuffsApp(
                viewModel = viewModel, // Pass the viewModel instance
                windowSize = WindowWidthSizeClass.Expanded
            )
        }
    }
}

