package com.example.haonv.data.remote.response

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("topalbums") val topAlbums: TopAlbums
)

data class TopAlbums(
    @SerializedName("album") val album: List<Album>,
    @SerializedName("@attr") val attr: AlbumAttr
)

data class Album(
    @SerializedName("name") val name: String? = null,
    @SerializedName("playcount") val playCount: Int? = null,
    @SerializedName("url") val url: String? = null,
    @SerializedName("artist") val artist: AlbumArtist,
    @SerializedName("image") val image: List<AlbumImage>
)

data class AlbumArtist(
    @SerializedName("name") val name: String? = null,
    @SerializedName("mbid") val mBid: String? = null,
    @SerializedName("url") val url: String? = null
)

data class AlbumImage(
    @SerializedName("#text") val text: String? = null,
    @SerializedName("size") val size: String? = null
)

data class AlbumAttr(
    @SerializedName("artist")
    val artist: String? = null,
    @SerializedName("page")
    val page: String? = null,
    @SerializedName("perPage")
    val perPage: String? = null,
    @SerializedName("totalPages")
    val totalPages: String? = null,
    @SerializedName("total")
    val total: String? = null
)




