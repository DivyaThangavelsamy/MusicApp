package com.lastfm.musicapp.model

import com.google.gson.annotations.SerializedName

data class ArtistDetails(
    @SerializedName("artist")
    val artist: Artist
)

data class Artist(
    @SerializedName("bio")
    val bio: Bio?,

    @SerializedName("stats")
    val statistics: Statistics
)

data class Bio(
    @SerializedName("summary")
    val summary: String?
)

data class Statistics(
    @SerializedName("playcount")
    val playCount: Int?
)


