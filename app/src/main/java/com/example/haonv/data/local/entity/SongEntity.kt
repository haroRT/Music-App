package com.example.haonv.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class Song(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "imageUrl") val imageURL: String,
    @ColumnInfo(name = "mp3Data") val mp3Data: String,
    @ColumnInfo(name = "duration") val duration: Long
)
