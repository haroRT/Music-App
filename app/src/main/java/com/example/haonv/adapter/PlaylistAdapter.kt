package com.example.haonv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.haonv.R

import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.entity.Song
import com.example.haonv.data.local.model.PlaylistWithCountSong
import com.example.haonv.databinding.ItemPlaylistBinding

class PlaylistAdapter(private val dataset: List<PlaylistWithCountSong>) :
    RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    private var listenerItem: OnItemClickListener? = null
    private var listenerMoreOption: OnItemMoreOptionClickListener? = null
    private var isItemSelect = false

    fun setOnItemMoreOptionClickListener(
        listenerMoreOption: OnItemMoreOptionClickListener,
    ) {
        this.listenerMoreOption = listenerMoreOption
    }

    fun setOnItemClickListener(listenerItem: OnItemClickListener) {
        this.listenerItem = listenerItem
    }

    fun setItemSelect(isItemSelect: Boolean) {
        this.isItemSelect = isItemSelect
    }

    class ViewHolder(val binding: ItemPlaylistBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistAdapter.ViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.binding.root.context)
            .load(R.drawable.img_song_default).centerCrop()
            .placeholder(R.drawable.img_song_default)
            .into(holder.binding.ivLogoPlaylist)
        holder.binding.tvItemPlaylistTitle.text = dataset[position].name
        holder.binding.cslPlaylist.setOnClickListener {
            listenerItem?.onItemClick(dataset[position].id)
        }
        holder.binding.ivMoreOption.setOnClickListener {
            listenerMoreOption?.onItemMoreOptionClick(
                holder.binding.ivMoreOption,
                position,
                dataset[position].id
            )
        }
        if (isItemSelect) {
            holder.binding.root.setBackgroundResource(R.color.grey_bg_dialog_select_playlist)
            holder.binding.ivMoreOption.visibility = View.INVISIBLE
        }
        holder.binding.tvItemPlaylistSize.text = "${dataset[position].songCount} songs"
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    interface OnItemMoreOptionClickListener {
        fun onItemMoreOptionClick(view: View, position: Int, playlistId: Int)
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }
}