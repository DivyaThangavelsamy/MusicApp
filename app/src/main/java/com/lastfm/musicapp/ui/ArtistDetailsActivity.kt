package com.lastfm.musicapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.lastfm.musicapp.R
import com.lastfm.musicapp.databinding.ActivityArtistDetailsBinding
import com.lastfm.musicapp.extension.isNetworkAvailable
import com.lastfm.musicapp.viewModel.ArtistDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArtistDetailsActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityArtistDetailsBinding

    private val viewModel: ArtistDetailsViewModel by viewModels()

    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_artist_details)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        coordinatorLayout = dataBinding.myCoordinatorLayout

        val artistDetails = intent

        val name = artistDetails.getStringExtra("name")
        supportActionBar?.setTitle(name)

        if (this.isNetworkAvailable()) {
            name?.let { viewModel.loadArtistsDetails(it) }
        } else
            showError("Please check your network")

        viewModel.errorData.observe(this, { status ->
            status.let {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showError(error: String) {
        Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG).show()
    }
}