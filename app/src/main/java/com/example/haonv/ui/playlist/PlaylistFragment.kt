package com.example.haonv.ui.playlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haonv.App
import com.example.haonv.R
import com.example.haonv.SharedPref
import com.example.haonv.adapter.PlaylistAdapter
import com.example.haonv.base.BaseFragment
import com.example.haonv.data.local.entity.Playlist
import com.example.haonv.data.local.entity.Song
import com.example.haonv.databinding.DialogAddSongToPlaylistBinding
import com.example.haonv.databinding.DialogPlaylistMoreOptionBinding
import com.example.haonv.databinding.FragmentPlaylistBinding
import com.example.haonv.di.DIContainer.viewModelContainer
import com.example.haonv.ui.playlist.dialog.DialogAddPlaylist
import com.example.haonv.ui.playlist.playlistdetail.PlaylistDetailFragment

class PlaylistFragment : BaseFragment<FragmentPlaylistBinding>() {
    private val playlistViewModel: PlaylistViewModel by lazy {
        viewModelContainer.getViewModel(
            this,
            PlaylistViewModel::class.java
        )
    }

    private var dialogAddPlaylist: DialogAddPlaylist? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlaylistBinding {
        return FragmentPlaylistBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        super.setupUI()

        dialogAddPlaylist = DialogAddPlaylist()

        if (arguments?.getBoolean("SHOW_POPUP") == true) {
            dialogAddPlaylist?.show(parentFragmentManager, "AddPlaylistDialog")
        }
    }

    override fun setupData() {
        super.setupData()

        playlistViewModel.getPlaylistByUserId()
    }

    override fun setupListener() {
        super.setupListener()

        dialogAddPlaylist?.setOnItemClickListener(object : DialogAddPlaylist.OnClickListener{
            override fun onClick(namePlaylist: String) {
                playlistViewModel.addPlaylist(namePlaylist)
            }
        })

        binding.ivAddPlaylist.setOnClickListener {
            dialogAddPlaylist?.show(parentFragmentManager, "AddPlaylistDialog")
        }
        binding.ivVisiblePopup.setOnClickListener {
            dialogAddPlaylist?.show(parentFragmentManager, "AddPlaylistDialog")
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        playlistViewModel.playlists.observe(viewLifecycleOwner, {
            val adapter = PlaylistAdapter(it)
            adapter.setOnItemClickListener(object : PlaylistAdapter.OnItemClickListener{
                override fun onItemClick(id: Int) {
                    val result = Bundle()
                    result.putInt("playlistId", id)
                    parentFragmentManager.setFragmentResult("requestKey", result)
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.llMain, PlaylistDetailFragment())
                        .addToBackStack(null)
                        .commit()
                }
            })
            adapter.setOnItemMoreOptionClickListener(object : PlaylistAdapter.OnItemMoreOptionClickListener{
                override fun onItemMoreOptionClick(view: View, position: Int, playlistId: Int) {
                    showPopupPlaylist(view, position, playlistId)
                }
            })
            binding.rvPlayList.layoutManager = LinearLayoutManager(context)
            binding.rvPlayList.adapter = adapter
        })

        playlistViewModel.playlistIsEmpty.observe(viewLifecycleOwner,{
            if (playlistViewModel.playlistIsEmpty.value == true){
                binding.grAddPlaylist.visibility = View.VISIBLE
                binding.rvPlayList.visibility = View.INVISIBLE
            } else {
                binding.grAddPlaylist.visibility = View.INVISIBLE
                binding.rvPlayList.visibility = View.VISIBLE
            }
        })
    }

    fun showPopupPlaylist(view: View, position: Int, playlistId: Int) {

        val binding = DialogPlaylistMoreOptionBinding.inflate(LayoutInflater.from(requireContext()))

        val popupWindow = PopupWindow(
            binding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        binding.tvRemovePlaylist.setOnClickListener {
            playlistViewModel.removePlaylist(playlistId)
            popupWindow.dismiss()
        }
        binding.tvRenamePlaylist.setOnClickListener{
            val addPlaylistDialog = DialogAddPlaylist()
            addPlaylistDialog.setOnItemClickListener(object : DialogAddPlaylist.OnClickListener{
                override fun onClick(namePlaylist: String) {
                    playlistViewModel.renamePlaylist(playlistId, namePlaylist)
                }
            })
            addPlaylistDialog.show(parentFragmentManager, "renamePlaylistDialog")
            popupWindow.dismiss()
        }
        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = true
        }
        popupWindow.showAsDropDown(
            view,
            view.width - 600,
            12
        )
    }

}