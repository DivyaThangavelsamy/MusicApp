package com.lastfm.musicapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lastfm.musicapp.model.ArtistDetails
import com.lastfm.musicapp.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
open class ArtistDetailsViewModel @Inject constructor(private val artistRepository: ArtistRepository) :
    ViewModel() {

    private var disposable: Disposable? = null

    private val _artistsDetails = MutableLiveData<ArtistDetails>()
    val artistsDetails: MutableLiveData<ArtistDetails> = _artistsDetails

    private val _artistsPlayCount = MutableLiveData<Int>()
    val artistsPlayCount: MutableLiveData<Int> = _artistsPlayCount

    private val _artistsSummary = MutableLiveData<String>()
    val artistsSummary: MutableLiveData<String> = _artistsSummary

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> = _errorData

    var progressBar = MutableLiveData<Boolean>()

    fun loadArtistsDetails(name: String) {
        loadProgressBar()
        disposable = artistRepository.getArtistDetails(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> onResponse(data) },
                { error -> onFailure(error) })
    }

    fun onFailure(error: Throwable?) {
        _errorData.postValue(error.toString())
        progressBar.value = false
    }

    fun onResponse(data: ArtistDetails) {
        setArtistDetails(data)
    }

    fun setArtistDetails(data: ArtistDetails) {
        _artistsDetails.postValue(data)
        _artistsPlayCount.postValue(data.artist.statistics.playCount ?: 0)
        _artistsSummary.postValue(data.artist.bio?.summary ?: "N/A")
        progressBar.value = false
    }

    fun loadProgressBar() {
        progressBar.value = true
    }
}