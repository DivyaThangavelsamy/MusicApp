package com.lastfm.musicapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lastfm.musicapp.model.Artists
import com.lastfm.musicapp.model.SearchArtists
import com.lastfm.musicapp.model.TopArtist
import com.lastfm.musicapp.repository.ArtistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val artistRepository: ArtistRepository) :
    ViewModel() {

    private var disposable: Disposable? = null

    private val _artists = MutableLiveData<List<TopArtist>>()
    val artists: MutableLiveData<List<TopArtist>> = _artists

    private val _errorData = MutableLiveData<String>()
    val errorData: LiveData<String> = _errorData

    var progressBar = MutableLiveData<Boolean>()

    fun loadArtists() {
        loadProgressBar()
        disposable = artistRepository.getTopArtist()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> onResponse(data) },
                { error -> onFailure(error) })
    }

    fun loadProgressBar() {
        progressBar.value = true
    }

    fun onFailure(error: Throwable?) {
        _errorData.postValue(error.toString())
        progressBar.value = false
    }

    fun onResponse(data: Artists) {
        setArtistDetails(data)
    }

    fun setArtistDetails(data: Artists) {
        _artists.postValue(data.artists.artist)
        progressBar.value = false
    }

    fun getSearchedArtist(artistName: String) {
        disposable = artistRepository.getArtistDetailsFromSearch(artistName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> onResponse(data) },
                { error -> onFailure(error) })
    }

    fun onResponse(data: SearchArtists) {
        _artists.postValue(data.artists.artists.artist)
        progressBar.value = false
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}