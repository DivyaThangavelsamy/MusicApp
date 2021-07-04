package com.lastfm.musicapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.lastfm.musicapp.databinding.ActivityArtistDetailsBinding
import com.lastfm.musicapp.extension.isNetworkAvailable
import com.lastfm.musicapp.model.Artist
import com.lastfm.musicapp.model.Artists
import com.lastfm.musicapp.viewModel.ArtistDetailsViewModel
import com.lastfm.musicapp.viewModel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityArtistDetailsBinding

    private val viewModel: ArtistDetailsViewModel by viewModels()

    private lateinit var artistName: TextView
    private lateinit var playCount: TextView
    private lateinit var artistInfo: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArtistDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        artistName = binding.artistName
        playCount = binding.playCount
        artistInfo = binding.artistInfo
        progressBar = binding.progressBar
        coordinatorLayout = binding.myCoordinatorLayout

        val artistDetails = intent

        val name = artistDetails.getStringExtra("name")
        supportActionBar?.setTitle(name)

        if (this.isNetworkAvailable()) {
            name?.let { viewModel.loadArtistsDetails(it) }
            startProgress()

            viewModel.artistsDetails.observe(this, Observer {
                artistName.text = name
                playCount.text = it.statistics.playCount?.toString() ?: "N/A"
                artistInfo.text = it.bio?.summary ?: "N/A"
                endProgress()
            })
        } else
            showError("Please check your network")

        viewModel.errorData.observe(this, { status ->
            endProgress()
            status.let {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun startProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun endProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showError(error: String) {
        Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG).show()
    }
}