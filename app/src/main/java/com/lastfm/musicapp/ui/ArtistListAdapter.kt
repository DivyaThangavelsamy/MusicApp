package com.lastfm.musicapp.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lastfm.musicapp.R
import com.lastfm.musicapp.databinding.ListArtistBinding
import com.lastfm.musicapp.model.TopArtist
import java.text.NumberFormat
import java.util.*
import javax.inject.Inject

class ArtistListAdapter @Inject constructor(private val context: Context) :

    RecyclerView.Adapter<ArtistListAdapter.ViewHolder>() {

    var artists = mutableListOf<TopArtist>()

    fun setArtistList(artists: List<TopArtist>) {
        this.artists = artists.toMutableList()
        notifyDataSetChanged()
    }

    class ViewHolder(val dataBinding: ListArtistBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, int: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val listItemBinding = ListArtistBinding.inflate(inflater, parent, false)
        return ViewHolder(listItemBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artists = artists[position]
        holder.dataBinding.artistName.text = artists.name
        "${(NumberFormat.getNumberInstance(Locale.US).format(artists.listeners?.toInt())) ?: "N/A"} listerners".also { holder.dataBinding.listerners.text = it }
        Glide.with(holder.itemView.context)
            .load(artists.image[2]?.text ?: R.drawable.ic_baseline_person_24)
            .into(holder.dataBinding.artistImage)
        holder.dataBinding.root.setOnClickListener {
            var intent = Intent(context, ArtistDetailsActivity::class.java)
            intent.putExtra("name", artists.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return artists.size
    }
}
