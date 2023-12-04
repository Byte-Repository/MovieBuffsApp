package com.example.moviebuffsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviebuffsapp.ui.MovieBuffsApp
import com.example.moviebuffsapp.ui.theme.MovieBuffsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MovieBuffsAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MovieBuffsApp()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieBuffsAppPreview() {
    MovieBuffsAppTheme {
        MovieBuffsApp()
    }
}

