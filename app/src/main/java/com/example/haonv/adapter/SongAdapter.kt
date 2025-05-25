package com.example.haonv.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.haonv.R
import com.example.haonv.data.local.entity.Song
import com.example.haonv.databinding.ItemSongGridBinding
import com.example.haonv.databinding.ItemSongLinearBinding
import com.example.haonv.utils.formatDuration

class SongAdapter(private var dataSet: List<Song>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listenerMoreOption: OnItemMoreOptionClickListener? = null
    private var listenerItem: OnItemClickListener? = null
    private var songSelected: Song? = null
    var isLinear = true
    var isEdit = false

    fun setSongSelected(song: Song?){
        songSelected = song
        notifyDataSetChanged()
    }

    fun setOnItemMoreOptionClickListener(
        listenerMoreOption: OnItemMoreOptionClickListener,
    ) {
        this.listenerMoreOption = listenerMoreOption
    }

    fun setOnItemClickListener(listenerItem: OnItemClickListener) {
        this.listenerItem = listenerItem
    }

    fun updateData(newData: List<Song>) {
        dataSet = newData
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_LINEAR = 0
        private const val TYPE_GRID = 1
    }

    fun setLayoutType() {
        isLinear = !isLinear
        notifyDataSetChanged()
    }

    fun setEdit() {
        isEdit = !isEdit
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return if (isLinear) TYPE_LINEAR else TYPE_GRID
    }

    class LinearViewHolder(val binding: ItemSongLinearBinding) :
        RecyclerView.ViewHolder(binding.root)

    class GridViewHolder(val binding: ItemSongGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == TYPE_LINEAR) {
            val binding = ItemSongLinearBinding
                .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return LinearViewHolder(binding)
        } else {
            val binding = ItemSongGridBinding
                .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
            return GridViewHolder(binding)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val item = dataSet[position]
        if (viewHolder is LinearViewHolder) {
            viewHolder.binding.tvItemTitle.text = item.name
            Glide.with(viewHolder.binding.root.context)
                .load(Uri.parse(item.imageURL))
                .centerCrop()
                .placeholder(R.drawable.img_song_default)
                .into(viewHolder.binding.ivLogoSong)
            viewHolder.binding.tvItemAuthor.text = dataSet[position].author
            viewHolder.binding.tvDuration.text = dataSet[position].duration.formatDuration()

            viewHolder.binding.ivMoreOption.setOnClickListener {
                listenerMoreOption?.onItemMoreOptionClick(viewHolder.binding.ivMoreOption, position, item)
            }

            viewHolder.binding.cdlSong.setOnClickListener {
                listenerItem?.onItemClick(dataSet[position], dataSet)
            }

            if (isEdit) {
                viewHolder.binding.ivMoreOption.setImageResource(R.drawable.ic_playlistdetail_move)
            } else {
                viewHolder.binding.ivMoreOption.setImageResource(R.drawable.ic_playlist_option)
            }
        } else if (viewHolder is GridViewHolder) {
            viewHolder.binding.tvItemTitle.text = item.name
            Glide.with(viewHolder.binding.root.context)
                .load(Uri.parse(item.imageURL))
                .centerCrop()
                .placeholder(R.drawable.img_song_default)
                .into(viewHolder.binding.ivLogoSong)
            viewHolder.binding.tvItemAuthor.text = dataSet[position].author
            viewHolder.binding.tvDuration.text = dataSet[position].duration.formatDuration()

            viewHolder.binding.ivMoreOption.setOnClickListener {
                listenerMoreOption?.onItemMoreOptionClick(viewHolder.binding.ivMoreOption, position, item)
            }

            viewHolder.binding.cslSongGrid.setOnClickListener {
                listenerItem?.onItemClick(dataSet[position], dataSet)
            }
            if (isEdit) {
                viewHolder.binding.ivMoreOption.setImageResource(R.drawable.ic_playlistdetail_move)
            } else {
                viewHolder.binding.ivMoreOption.setImageResource(R.drawable.ic_playlist_option)
            }
        }

        if(songSelected != null && songSelected?.name == item.name && songSelected?.id == item.id){
            viewHolder.itemView.setBackgroundResource(R.color.grey_bg_dialog_select_playlist)
        }else{
            viewHolder.itemView.setBackgroundResource(R.color.bg_color)
        }

    }

    override fun getItemCount() = dataSet.size

    interface OnItemMoreOptionClickListener {
        fun onItemMoreOptionClick(view: View, position: Int, song: Song)
    }

    interface OnItemClickListener {
        fun onItemClick(song: Song, listSong: List<Song>)
    }

}