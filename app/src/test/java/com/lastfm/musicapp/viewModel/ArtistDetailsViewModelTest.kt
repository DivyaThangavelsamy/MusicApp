package com.lastfm.musicapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lastfm.musicapp.model.Artist
import com.lastfm.musicapp.model.ArtistDetails
import com.lastfm.musicapp.model.Bio
import com.lastfm.musicapp.model.Statistics
import com.lastfm.musicapp.repository.ArtistRepository
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub

class ArtistDetailsViewModelTest {

    private companion object {
        const val PlAY_COUNT: String = "2145667"
        const val PLAY_COUNT_FORMATTED : String = "2,145,667"
        const val SUMMARY: String =
            "Cheryl Ann Fernandez-Versini (née Tweedy, known professionally as Cole; born June 30, 1983 in Newcastle upon Tyne, England) is an English singer, songwriter, dancer, and model. She is one of the five members of girl group Girls Aloud.\n\nRising to fame in late 2002, Cole was selected to become a member of girl group Girls Aloud on ITV's reality television programme Popstars The Rivals. Cole's first solo performance was on American rapper will.i.am's Heartbreaker in 2008. She was picked to appear as a dancer in the video after taking streetdancing classes during the filming of the ITV2 series The Passions of Girls Aloud, in which the members of the band achieve something they had always wanted to do, other than sing. She was later asked, by will.i.am, to sing additional vocals on the track.\n\nIn April 2009, Cole started working on solo material. Her debut album, 3 Words, was released in the UK on 26 October 2009. Cole was reunited with will.i.am for the album, in addition to collaborating with Fraser T. Smith, Syience, Danish production team Soulshock & Karlin, singer-songwriter Taio Cruz, Wayne Wilkins, and Steve Kipner. Recorded in Los Angeles and London, Cole mainly collaborated with will.i.am of the Black Eyed Peas, the executive producer of 3 Words. It crosses from contemporary R&B, dance pop, house and a more general pop sound.\n\n3 Words spent two weeks at number one. On 6 November 2009, BPI certified the album Platinum, denoting shipments of over 300,000 units. It later tripled this feat. The first single from the album, Fight for This Love, was written by Andre Merritt, Steve Kipner and Wayne Wilkins, and produced by Steve Kipner and Wayne Wilkins. According to Cole, it was released as the lead single because she \"connected with the song so well\". Following a performance on The X Factor live results show, Fight for This Love became the fourth best-selling single of 2009 in the UK. It charted at number one on both the Irish and UK Singles Chart. In 2010, Fight For This Love went to number one in Denmark, Norway and Hungary. The single was later certified platinum in the UK."
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var artistRepository: ArtistRepository

    @Mock
    private lateinit var mockArtistDetails: ArtistDetails

    @Mock
    private lateinit var mockArtist: Artist

    @Mock
    private lateinit var mockArtistBio: Bio

    @Mock
    private lateinit var mockArtistStatistics: Statistics

    @Mock
    private lateinit var exception: Exception

    lateinit var playCount : String

    private lateinit var viewModel: ArtistDetailsViewModel

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        mockArtistDetails.stub {
            on { artist } doReturn mockArtist
        }

        mockArtist.stub {
            on { bio } doReturn mockArtistBio
        }

        mockArtist.stub {
            on { statistics } doReturn mockArtistStatistics
        }

        mockArtistBio.stub {
            on { summary } doReturn SUMMARY
        }

        viewModel = ArtistDetailsViewModel(artistRepository)
    }

    @Test
    fun `given mockArtistDetails on setArtistDetails method then liveData updated with artistDetails`() {
        viewModel.setArtistDetails(mockArtistDetails)
        assertEquals(mockArtistDetails, viewModel.artistsDetails.value)
    }

    @Test
    fun `given playCount value when setArtistDetails method is called then liveData updated with artistPlayCount successfully`() {
        mockArtistStatistics.stub {
            on { playCount } doReturn PlAY_COUNT.toInt()
        }
        viewModel.setArtistDetails(mockArtistDetails)


        assertEquals(PLAY_COUNT_FORMATTED, viewModel.artistsPlayCount.value)
    }

    @Test
    fun `given playCount value to null when setArtistDetails method is called then liveData updated with artistPlayCount to 0`() {

        mockArtistStatistics.stub {
            on { playCount } doReturn null
        }
        viewModel.setArtistDetails(mockArtistDetails)

        assertEquals("N/A", viewModel.artistsPlayCount.value)
    }

    @Test
    fun `when loadartistDetails method returns error onFailure method is called then livedata updated with errorData`() {
        viewModel.onFailure(exception)
        assertEquals("exception", viewModel.errorData.value)
    }

    @Test
    fun `when loadProgress method called then assert progressBar value to true`() {
        viewModel.loadProgressBar()
        Assert.assertTrue("true", viewModel.progressBar.value == true)
    }

    @Test
    fun `given summary value to null when setArtistDetails method is called then liveData updated with artistsSummary`() {
        mockArtistBio.stub {
            on { summary } doReturn null
        }
        viewModel.setArtistDetails(mockArtistDetails)

        assertEquals("N/A", viewModel.artistsSummary.value)
    }

    @Test
    fun `given summary value when setArtistDetails method is called then liveData updated with artistSummarysuccessfully`() {
        viewModel.setArtistDetails(mockArtistDetails)

        mockArtistBio.stub {
            on { summary } doReturn SUMMARY
        }
        assertEquals(SUMMARY, viewModel.artistsSummary.value)
    }
}