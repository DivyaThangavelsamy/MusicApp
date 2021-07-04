package com.lastfm.musicapp.repository

import com.lastfm.musicapp.model.ArtistDetails
import com.lastfm.musicapp.model.Artists
import com.lastfm.musicapp.model.SearchArtists
import com.lastfm.musicapp.service.WebService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

interface MainRepository {
    fun getTopArtist() : Single<Artists>

    fun getArtistDetails(name: String) : Single<ArtistDetails>

    fun getArtistDetailsFromSerach(name: String) : Single<SearchArtists>
}

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val webService: WebService
) : MainRepository {
    override fun getTopArtist(): Single<Artists> {
        return webService
            .getTopArtist()
            .subscribeOn(Schedulers.io())
    }

    override fun getArtistDetails(name : String): Single<ArtistDetails> {
        return webService
            .getArtistDetails(name)
            .subscribeOn(Schedulers.io())
    }

    override fun getArtistDetailsFromSerach(name: String): Single<SearchArtists> {
        return webService
            .getArtistDetailsFromSerach(name)
            .subscribeOn(Schedulers.io())
    }

}