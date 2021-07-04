package com.lastfm.musicapp.model

import com.google.gson.annotations.SerializedName

data class Artists(
    @SerializedName("artists")
    val artists : ArtistsList
)

data class SearchArtists(
    @SerializedName("results")
    val artists : Matchresults
)


data class Matchresults(
    @SerializedName("artistmatches")
    val artists : ArtistsList
)

data class ArtistsList(
    @SerializedName("artist")
    val artist : List<TopArtist>
)

data class TopArtist(
    @SerializedName("name")
    val name : String,

    @SerializedName("listeners")
    val listeners : String?,

    @SerializedName("mbid")
    val mbid: String?,

    @SerializedName("image")
    val image: List<Image>
)

data class Image(
    @SerializedName("#text")
    val text : String?,

    @SerializedName("size")
    val size : String?,
)
