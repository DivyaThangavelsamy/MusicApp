package com.lastfm.musicapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lastfm.musicapp.model.*
import com.lastfm.musicapp.repository.ArtistRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

class MainActivityViewModelTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    @Mock
    private lateinit var artistRepository: ArtistRepository

    @Mock
    private lateinit var mockArtistData: Artists

    @Mock
    private lateinit var mockSearchArtistData: SearchArtists

    @Mock
    private lateinit var mockSearchArtistMatchResult: Matchresults

    @Mock
    private lateinit var mockArtists: ArtistsList

    @Mock
    private lateinit var mockTopArtist: List<TopArtist>

    @Mock
    private lateinit var exception: Exception

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mockArtists.stub {
            on { artist } doReturn mockTopArtist
        }
        mockArtistData.stub {
            on { artists } doReturn mockArtists
        }

        mockSearchArtistData.stub {
            on { artists } doReturn mockSearchArtistMatchResult
        }

        mockSearchArtistMatchResult.stub {
            on { artists } doReturn mockArtists
        }
        viewModel = MainActivityViewModel(artistRepository)
    }

    @Test
    fun `given mockArtistData on setArtistDetails method set liveData artist` () {

        viewModel.setArtistDetails(mockArtistData)
        assertEquals(mockTopArtist, viewModel.artists.value)
    }

    @Test
    fun `when loadProgress method called then assert progressBar value to true` () {
        viewModel.loadProgressBar()
        Assert.assertTrue("true", viewModel.progressBar.value == true)
    }

    @Test
    fun `when loadartist method returns error onFailure method is called then livedata updated with errorData` () {
        viewModel.onFailure(exception)
        assertEquals("exception", viewModel.errorData.value)
    }

    @Test
    fun `when loadartist method returns data onResponse method is called then livedata updated with artists` () {
        viewModel.onResponse(mockSearchArtistData)
        assertEquals(mockTopArtist, viewModel.artists.value)
    }
}