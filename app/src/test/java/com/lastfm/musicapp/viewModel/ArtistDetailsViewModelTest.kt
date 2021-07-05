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
        const val PlAY_COUNT: Int = 2145667
        const val SUMMARY: String =
            "Cheryl Ann Fernandez-Versini (née Tweedy, known professionally as Cole; born June 30, 1983 in Newcastle upon Tyne, England) is an English singer, songwriter, dancer, and model. She is one of the five members of girl group Girls Aloud.\n\nRising to fame in late 2002, Cole was selected to become a member of girl group Girls Aloud on ITV's reality television programme Popstars The Rivals. Cole's first solo performance was on American rapper will.i.am's Heartbreaker in 2008. She was picked to appear as a dancer in the video after taking streetdancing classes during the filming of the ITV2 series The Passions of Girls Aloud, in which the members of the band achieve something they had always wanted to do, other than sing. She was later asked, by will.i.am, to sing additional vocals on the track.\n\nIn April 2009, Cole started working on solo material. Her debut album, 3 Words, was released in the UK on 26 October 2009. Cole was reunited with will.i.am for the album, in addition to collaborating with Fraser T. Smith, Syience, Danish production team Soulshock & Karlin, singer-songwriter Taio Cruz, Wayne Wilkins, and Steve Kipner. Recorded in Los Angeles and London, Cole mainly collaborated with will.i.am of the Black Eyed Peas, the executive producer of 3 Words. It crosses from contemporary R&B, dance pop, house and a more general pop sound.\n\n3 Words spent two weeks at number one. On 6 November 2009, BPI certified the album Platinum, denoting shipments of over 300,000 units. It later tripled this feat. The first single from the album, Fight for This Love, was written by Andre Merritt, Steve Kipner and Wayne Wilkins, and produced by Steve Kipner and Wayne Wilkins. According to Cole, it was released as the lead single because she \"connected with the song so well\". Following a performance on The X Factor live results show, Fight for This Love became the fourth best-selling single of 2009 in the UK. It charted at number one on both the Irish and UK Singles Chart. In 2010, Fight For This Love went to number one in Denmark, Norway and Hungary. The single was later certified platinum in the UK. Cole's second single 3 Words, which features will.i.am, went to number 4 in the UK and seven in Ireland. In 2010, the single was released in Australia and charted at number 5 and was certified platinum. The third single, Parachute, charted in the top five in both the UK and Ireland. The single was certified silver in the UK. From May to July 2010, Cole was the opening act for The Black Eyed Peas at the British shows (as well as some European dates) of The E.N.D. World Tour. Chris Johnson of the Daily Mail wrote, \"she was supposed to be the support act. But as it turned out, Cheryl Cole ended up being the main event\".\n\nIn March 2010, Cole stated that she had begun working on a follow up album to 3 Words, which she hoped to release \"later on in the year\". Cole's second solo album, titled Messy Little Raindrops, was released on 1 November 2010. The album's first single, Promise This, was released on 24 October 2010 and became her second number-one hit in the UK. The album was largely produced by Wayne Wilkins, but Cole also reunited with will.i.am, with collaboration from J. R. Rotem, Starsmith, Al Shux, and Free School for the first time. The album also features guest vocals from August Rigo, Dizzee Rascal, Travie McCoy, and will.i.am. The album's lead single, Promise This, went straight to number 1 in the UK, selling 157,000 in its first week. A week later, Messy Little Raindrops was released, charting at number 1 in the UK, 2 in Ireland and 7 in Europe. <a href=\"https://www.last.fm/music/Cheryl+Cole\">Read more on Last.fm</a>. User-contributed text is available under the Creative Commons By-SA License; additional terms may apply."
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
            on { playCount } doReturn PlAY_COUNT
        }
        viewModel.setArtistDetails(mockArtistDetails)

        assertEquals(PlAY_COUNT, viewModel.artistsPlayCount.value)
    }

    @Test
    fun `given playCount value to null when setArtistDetails method is called then liveData updated with artistPlayCount to 0`() {
        mockArtistStatistics.stub {
            on { playCount } doReturn null
        }
        viewModel.setArtistDetails(mockArtistDetails)

        assertEquals(0, viewModel.artistsPlayCount.value)
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