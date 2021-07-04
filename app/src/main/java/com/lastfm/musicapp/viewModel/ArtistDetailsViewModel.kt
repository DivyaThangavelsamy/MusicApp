package com.lastfm.musicapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lastfm.musicapp.model.Artist
import com.lastfm.musicapp.model.ArtistDetails
import com.lastfm.musicapp.model.Bio
import com.lastfm.musicapp.repository.MainRepository
import com.lastfm.musicapp.service.WebService
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class ArtistDetailsViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel(){

    private var disposable: Disposable? = null

    private val _artistsDetails = MutableLiveData<Artist>()
    val artistsDetails: MutableLiveData<Artist> = _artistsDetails

    private val _errorData  = MutableLiveData<String>()
    val errorData : LiveData<String> = _errorData

    fun loadArtistsDetails(name : String) {
        disposable = mainRepository.getArtistDetails(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> onResponse(data) },
                { error -> onFailure(error) })
    }

    private fun onFailure(error: Throwable?) {
        _errorData.postValue(error.toString())
        Log.d("Error", error.toString())
    }

    private fun onResponse(data: ArtistDetails) {
        _artistsDetails.postValue((data.artist))
    }
}