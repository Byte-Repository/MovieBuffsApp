package com.example.moviebuffsapp.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Movies(
    val title: String,
    val poster: String,
    val description: String,
    @SerialName("release_date")
    val releaseDate: String,
    @SerialName("content_rating")
    val contentRating: String,
    @SerialName("review_score")
    val reviewScore: String,
    @SerialName("big_image")
    val bigImage: String,
    val length: String
)
