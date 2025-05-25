package com.example.haonv.data.remote.response

import com.google.gson.annotations.SerializedName

data class ArtistResponse (

    @SerializedName("artists" ) var artists : Artists?

)

data class ArtistImage (

    @SerializedName("#text" ) var text : String? = null,
    @SerializedName("size"  ) var size  : String? = null

)

data class Artist (

    @SerializedName("name"       ) var name       : String?          = null,
    @SerializedName("playcount"  ) var playCount  : String?          = null,
    @SerializedName("listeners"  ) var listeners  : String?          = null,
    @SerializedName("mbid"       ) var mBid       : String?          = null,
    @SerializedName("url"        ) var url        : String?          = null,
    @SerializedName("streamable" ) var streamable : String?          = null,
    @SerializedName("image"      ) var image      : List<ArtistImage>

)

data class ArtistAttr (

@SerializedName("page"       ) var page       : String? = null,
@SerializedName("perPage"    ) var perPage    : String? = null,
@SerializedName("totalPages" ) var totalPages : String? = null,
@SerializedName("total"      ) var total      : String? = null

)

data class Artists (
    @SerializedName("artist" ) var artist : List<Artist>,
    @SerializedName("@attr"  ) var attr  : ArtistAttr?

)
