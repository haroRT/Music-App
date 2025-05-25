package com.example.haonv.data.remote

import com.example.haonv.data.remote.response.AlbumResponse
import com.example.haonv.data.remote.response.ArtistResponse
import com.example.haonv.data.remote.response.SongResponse
import com.example.haonv.data.remote.response.TrackResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApi {
    @GET("techtrek/Remote_audio.json")
    fun getSongsFromRemote(): Call<List<SongResponse>>
}

interface RankApi {
    @GET(
        "2.0/?api_key=e65449d181214f936368984d4f4d4ae8" +
                "&format=json&method=artist.getTopAlbums" +
                "&mbid=f9b593e6-4503-414c-99a0-46595ecd2e23"
    )
    suspend fun getTopAlbum(@Query("limit") limit: Int?): Response<AlbumResponse>

    @GET(
        "2.0/?api_key=e65449d181214f936368984d4f4d4ae8" +
                "&format=json&method=artist.getTopTracks" +
                "&mbid=f9b593e6-4503-414c-99a0-46595ecd2e23"
    )
    suspend fun getTopTrack(@Query("limit") limit: Int?): Response<TrackResponse>

    @GET(
        "2.0/?api_key=e65449d181214f936368984d4f4d4ae8" +
                "&format=json&method=chart.gettopartists"
    )
    suspend fun getTopArtist(@Query("limit") limit: Int?): Response<ArtistResponse>

}

