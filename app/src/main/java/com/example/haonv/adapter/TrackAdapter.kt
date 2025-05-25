package com.example.haonv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.haonv.R
import com.example.haonv.adapter.TrackAdapter.GridViewHolder
import com.example.haonv.data.remote.response.Track
import com.example.haonv.databinding.ItemAlbumGridBinding
import com.example.haonv.databinding.ItemTrackGridBinding

class TrackAdapter(private val dataset: List<Track>) :
    RecyclerView.Adapter<GridViewHolder>() {

    class GridViewHolder(val binding: ItemTrackGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding =
            ItemTrackGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(holder.itemView.context)
            .load(item.trackImage[0].text)
            .error(R.drawable.img_song_default)
            .into(holder.binding.ivTrack)
        holder.binding.tvNameTrack.text = item.name
        holder.binding.tvListens.text = item.listeners.toString()
        holder.binding.tvNameSinger.text = item.trackArtist?.name
        when(position % 8){
            0 -> holder.binding.vColor.setBackgroundResource(R.color.light_red_track)
            1 -> holder.binding.vColor.setBackgroundResource(R.color.yellow_track)
            2 -> holder.binding.vColor.setBackgroundResource(R.color.blue_track)
            3 -> holder.binding.vColor.setBackgroundResource(R.color.green_track)
            4 -> holder.binding.vColor.setBackgroundResource(R.color.purple_track)
            5 -> holder.binding.vColor.setBackgroundResource(R.color.cyan_track)
            6 -> holder.binding.vColor.setBackgroundResource(R.color.red_track)
            7 -> holder.binding.vColor.setBackgroundResource(R.color.light_track)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}