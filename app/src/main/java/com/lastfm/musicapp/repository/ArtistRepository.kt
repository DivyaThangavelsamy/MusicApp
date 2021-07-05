package com.lastfm.musicapp.repository

import com.lastfm.musicapp.model.ArtistDetails
import com.lastfm.musicapp.model.Artists
import com.lastfm.musicapp.model.SearchArtists
import com.lastfm.musicapp.service.WebService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface ArtistRepository {
    fun getTopArtist(): Single<Artists>

    fun getArtistDetails(name: String): Single<ArtistDetails>

    fun getArtistDetailsFromSearch(name: String): Single<SearchArtists>
}

class ArtistRepositoryImpl @Inject constructor(
    private val webService: WebService
) : ArtistRepository {
    override fun getTopArtist(): Single<Artists> {
        return webService
            .getTopArtist()
            .subscribeOn(Schedulers.io())
    }

    override fun getArtistDetails(name: String): Single<ArtistDetails> {
        return webService
            .getArtistDetails(name)
            .subscribeOn(Schedulers.io())
    }

    override fun getArtistDetailsFromSearch(name: String): Single<SearchArtists> {
        return webService
            .getArtistDetailsFromSearch(name)
            .subscribeOn(Schedulers.io())
    }
}