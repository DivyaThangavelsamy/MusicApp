package com.lastfm.musicapp.ui

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.lastfm.musicapp.R
import com.lastfm.musicapp.databinding.ActivityMainBinding
import com.lastfm.musicapp.viewModel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lastfm.musicapp.extension.isNetworkAvailable

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var adapter: ArtistListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recylerView
        adapter = ArtistListAdapter(this)
        recyclerView.adapter = adapter
        emptyText = binding.emptyTxt
        progressBar = binding.progressBar
        coordinatorLayout = binding.myCoordinatorLayout

        if (this.isNetworkAvailable()) {
            viewModel.loadArtists()
            startProgress()
            viewModel.artists.observe(this, {
                showEmptyList(it.isEmpty())
                adapter.setArtistList(it)
                recyclerView.visibility = View.VISIBLE
                endProgress()
            })
        } else
            showError("Your Offline")


        viewModel.errorData.observe(this, { status ->
            endProgress()
            status.let {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search_icon)
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.getSearchedArtist(query.trim())
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    fun startProgress() {
        emptyText.visibility = View.INVISIBLE
        progressBar.visibility = View.VISIBLE
    }

    fun endProgress() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showEmptyList(showList: Boolean) {
        if (!showList) {
            recyclerView.visibility = View.VISIBLE
            emptyText.visibility = View.GONE
        } else {
            recyclerView.visibility = View.GONE
            emptyText.visibility = View.VISIBLE
        }
    }

    private fun showError(error: String) {
        Snackbar.make(coordinatorLayout, error, Snackbar.LENGTH_LONG).show()
    }

}
