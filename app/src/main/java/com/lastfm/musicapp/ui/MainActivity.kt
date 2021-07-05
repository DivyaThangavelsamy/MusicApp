package com.lastfm.musicapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lastfm.musicapp.R
import com.lastfm.musicapp.databinding.ActivityMainBinding
import com.lastfm.musicapp.extension.isNetworkAvailable
import com.lastfm.musicapp.viewModel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivityMainBinding

    private val viewModel: MainActivityViewModel by viewModels()

    private lateinit var adapter: ArtistListAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var coordinatorLayout: CoordinatorLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        recyclerView = dataBinding.recylerView
        adapter = ArtistListAdapter(this)
        recyclerView.adapter = adapter
        emptyText = dataBinding.emptyTxt
        coordinatorLayout = dataBinding.myCoordinatorLayout

        if (this.isNetworkAvailable()) {
            viewModel.loadArtists()
            viewModel.artists.observe(this, {
                showEmptyList(it.isEmpty())
                adapter.setArtistList(it)
                recyclerView.visibility = View.VISIBLE
            })
        } else
            showError("Your Offline")

        viewModel.errorData.observe(this, { status ->
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
