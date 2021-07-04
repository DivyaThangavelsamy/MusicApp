package com.lastfm.musicapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lastfm.musicapp.model.*
import com.lastfm.musicapp.repository.MainRepository
import com.lastfm.musicapp.service.WebService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val mainRepository: MainRepository)  : ViewModel() {

    private var disposable: Disposable? = null

    private val _artists = MutableLiveData<List<TopArtist>>()
    val artists: MutableLiveData<List<TopArtist>> = _artists

    private val _errorData  = MutableLiveData<String>()
    val errorData : LiveData<String> = _errorData

    fun loadArtists() {
        disposable = mainRepository.getTopArtist()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> onResponse(data) },
                { error -> onFailure(error) })
    }

    private fun onFailure(error: Throwable?) {
        _errorData.postValue(error.toString())
        Log.d("Error", error.toString())
    }

    private fun onResponse(data: Artists) {
        _artists.postValue(data.artists.artist)
        Log.d("Result", data.artists.artist[0].toString())
    }


    fun getSearchedArtist(artistName : String){
        disposable = mainRepository.getArtistDetailsFromSerach(artistName)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                { data -> onResponse(data) },
                { error ->onFailure(error) })
    }

    private fun onResponse(data: SearchArtists) {
        _artists.postValue(data.artists.artists.artist)
        Log.d("Result", data.toString())
    }
}