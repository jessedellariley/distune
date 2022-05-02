package com.example.distune

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class TrackAdapter(val context: Context, private val tracks: List<Track>) : RecyclerView.Adapter<TrackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Specify the layout file to use for this item
        val view = LayoutInflater.from(context).inflate(R.layout.item_track,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val tvSongTitle: TextView = itemView.findViewById(R.id.tvSongTitle)
        private val ivAlbumCover: ImageView = itemView.findViewById(R.id.ivAlbumCover)
        private val tvArtist: TextView = itemView.findViewById(R.id.tvArtist)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(song: Track) {
            tvSongTitle.text = song.getTitle()
            tvArtist.text = song.getArtist()
            Glide.with(itemView.context).load(song.getImage()).into(ivAlbumCover)
        }

        override fun onClick(p0: View?) {
            val track = tracks[adapterPosition].getId()
            // change the color of the song to show that it is playing
            tvSongTitle.setTextColor(Color.parseColor("#FF57B55A"))
            // start playing the song

        }
    }
}