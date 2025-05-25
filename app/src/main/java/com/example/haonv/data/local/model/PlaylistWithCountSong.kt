package com.example.haonv.data.local.model


data class PlaylistWithCountSong(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val description: String?,
    val userId: Int,
    val songCount: Int
)
