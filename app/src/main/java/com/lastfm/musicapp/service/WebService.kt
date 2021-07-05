package com.lastfm.musicapp.service

import com.lastfm.musicapp.model.ArtistDetails
import com.lastfm.musicapp.model.Artists
import com.lastfm.musicapp.model.SearchArtists
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WebService {

    @GET("?method=chart.gettopartists")
    fun getTopArtist(): Single<Artists>

    @GET("?method=artist.getinfo")
    fun getArtistDetails(@Query("artist") name: String): Single<ArtistDetails>

    @GET("?method=artist.search")
    fun getArtistDetailsFromSearch(@Query("artist") name: String): Single<SearchArtists>

}