package com.example.haonv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.haonv.R
import com.example.haonv.data.remote.response.Album
import com.example.haonv.databinding.ItemAlbumGridBinding
import com.example.haonv.databinding.ItemAlbumLinearBinding

class AlbumAdapter(
    private val dataset: List<Album>,
    private val onClick: (Album) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var isLinear = true

    companion object {
        private const val TYPE_LINEAR = 0
        private const val TYPE_GRID = 1
    }

    fun setLayoutType() {
        isLinear = !isLinear
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLinear) TYPE_LINEAR else TYPE_GRID
    }

    class LinearViewHolder(val binding: ItemAlbumLinearBinding) :
        RecyclerView.ViewHolder(binding.root)

    class GridViewHolder(val binding: ItemAlbumGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_LINEAR) {
            val binding =
                ItemAlbumLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return LinearViewHolder(binding)
        } else {
            val binding =
                ItemAlbumGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return GridViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = dataset[position]
        if (holder is GridViewHolder) {
            Glide.with(holder.binding.root.context)
                .load(item.image[0].text)
                .centerCrop()
                .error(R.drawable.img_song_default)
                .into(holder.binding.ivAlbum)
            holder.binding.tvTitleAlbum.text = item.name
            holder.binding.tvAuthorAlbum.text = item.artist.name
            holder.binding.root.setOnClickListener{
                onClick(item)
            }
        } else if (holder is LinearViewHolder) {
            Glide.with(holder.binding.root.context)
                .load(item.image[0].text)
                .centerCrop()
                .error(R.drawable.img_song_default)
                .into(holder.binding.ivAlbum)
            holder.binding.tvTitleAlbum.text = item.name
            holder.binding.tvAuthorAlbum.text = item.artist.name
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}