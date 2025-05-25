package com.example.haonv.data.remote.response

import com.google.gson.annotations.SerializedName

data class SongResponse(
    @SerializedName("title")
    val name: String,
    @SerializedName("artist")
    val author: String,
    @SerializedName("path")
    val mp3Data: String,
    @SerializedName("duration")
    val duration: Long
)