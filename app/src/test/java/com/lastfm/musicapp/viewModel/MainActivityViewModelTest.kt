package com.lastfm.musicapp.viewModel

import com.lastfm.musicapp.model.TopArtist
import com.lastfm.musicapp.repository.MainRepository
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.MockitoKotlinException

class MainActivityViewModelTest {

    @Mock
    private lateinit var mainRepository: MainRepository

    @Mock
    private lateinit var mockArtistData: List<TopArtist>

    @Mock
    private lateinit var viewModel: MainActivityViewModel


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MainActivityViewModel(mainRepository)
    }

    @Test
    fun getArtists() {
    }

    @Test
    fun getErrorData() {
    }

    @Test
    fun loadArtists() {

    }

    @Test
    fun getSearchedArtist() {
    }
}