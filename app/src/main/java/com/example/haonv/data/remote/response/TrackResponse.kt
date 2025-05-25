package com.example.haonv.data.remote.response

import com.google.gson.annotations.SerializedName

data class TrackResponse(

    @SerializedName("toptracks") var topTracks: TopTracks

)

data class TrackArtist(

    @SerializedName("name") var name: String? = null,
    @SerializedName("mbid") var mBid: String? = null,
    @SerializedName("url") var url: String? = null

)

data class TrackImage(

    @SerializedName("#text") var text: String? = null,
    @SerializedName("size") var size: String? = null

)

data class AttrRank(

    @SerializedName("rank") var rank: String? = null

)

data class Track(

    @SerializedName("name") var name: String? = null,
    @SerializedName("playcount") var playcount: String? = null,
    @SerializedName("listeners") var listeners: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("streamable") var streamable: String? = null,
    @SerializedName("artist") var trackArtist: TrackArtist?,
    @SerializedName("image") var trackImage: List<TrackImage>,
    @SerializedName("@attr") var attr: AttrRank

)

data class TrackAttr(

    @SerializedName("artist") var artist: String? = null,
    @SerializedName("page") var page: String? = null,
    @SerializedName("perPage") var perPage: String? = null,
    @SerializedName("totalPages") var totalPages: String? = null,
    @SerializedName("total") var total: String? = null

)

data class TopTracks(

    @SerializedName("track") var track: List<Track>,
    @SerializedName("@attr") var trackAttr: TrackAttr?

)