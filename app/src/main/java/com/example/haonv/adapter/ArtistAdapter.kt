package com.example.haonv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.haonv.R
import com.example.haonv.data.remote.response.Artist
import com.example.haonv.databinding.ItemArtistGridBinding

class ArtistAdapter(private val dataset: List<Artist>) :
    RecyclerView.Adapter<ArtistAdapter.GridViewHolder>() {

    class GridViewHolder(val binding: ItemArtistGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridViewHolder {
        val binding =
            ItemArtistGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val item = dataset[position]
        Glide.with(holder.itemView.context)
            .load(item.image[0].text)
            .error(R.drawable.img_song_default)
            .into(holder.binding.ivArtist)
        holder.binding.tvNameArtist.text = item.name
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}