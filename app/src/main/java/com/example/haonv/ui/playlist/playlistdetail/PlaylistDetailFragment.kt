package com.example.haonv.ui.playlist.playlistdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.haonv.App
import com.example.haonv.adapter.SongAdapter
import com.example.haonv.data.local.entity.Song
import com.example.haonv.databinding.DialogPlaylistMoreOptionBinding
import com.example.haonv.databinding.DialogPlaylistdetailMoreOptionBinding
import com.example.haonv.databinding.FragmentPlaylistDetailBinding
import com.example.haonv.ui.main.MainViewModel
import com.example.haonv.ui.playlist.dialog.DialogAddPlaylist
import java.util.Collections

class PlaylistDetailFragment : Fragment() {
    private var _binding: FragmentPlaylistDetailBinding? = null
    private val binding get() = _binding!!
    private val listSongs: MutableList<Song> = mutableListOf()
    private lateinit var adapter: SongAdapter
    private var itemTouchHelper: ItemTouchHelper? = null
    private val playlistDetailViewModel: PlaylistDetailViewModel by viewModels {
        PlaylistDetailViewModel.PlaylistDetailViewModelFactory(requireActivity().application as App)
    }
    private val mainViewModel: MainViewModel by activityViewModels()

    private fun setupItemTouchHelper() {
        if (!adapter.isEdit) {
            itemTouchHelper?.attachToRecyclerView(null)
            return
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.START or ItemTouchHelper.END, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                Collections.swap(listSongs, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            }
        }
        itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper!!.attachToRecyclerView(binding.rvPlayList)
    }

    fun showPopupPlaylist(view: View, position: Int, songId: Int) {

        val binding =
            DialogPlaylistdetailMoreOptionBinding.inflate(LayoutInflater.from(requireContext()))

        val popupWindow = PopupWindow(
            binding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        binding.tvRemoveSong.setOnClickListener {
            if (playlistDetailViewModel.playlistId.value != null) {
                playlistDetailViewModel.removeSongFromPlaylist(
                    playlistDetailViewModel.playlistId.value!!,
                    songId
                )
                playlistDetailViewModel.loadListSong(playlistDetailViewModel.playlistId.value!!)
            }
            popupWindow.dismiss()
        }
        binding.tvShareSong.setOnClickListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener("requestKey", this)
        { _, bundle ->
            val receivedData = bundle.getInt("playlistId")
            playlistDetailViewModel.setPlaylistId(receivedData)
            playlistDetailViewModel.loadListSong(receivedData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlayList.layoutManager = LinearLayoutManager(context)
        if (playlistDetailViewModel.song.value != null){
            adapter.setSongSelected(playlistDetailViewModel.song.value!!)
        }
        adapter = SongAdapter(listSongs)
        adapter.setOnItemClickListener(object : SongAdapter.OnItemClickListener {
            override fun onItemClick(song: Song, listSong: List<Song>) {
                mainViewModel.setSong(song)
                mainViewModel.setListSong(listSong)
            }
        })
        adapter.setOnItemMoreOptionClickListener(object :
            SongAdapter.OnItemMoreOptionClickListener {
            override fun onItemMoreOptionClick(view: View, position: Int, song: Song) {
                showPopupPlaylist(view, position, song.id)
            }
        })
        binding.rvPlayList.adapter = adapter

        binding.ivChangeGrid.setOnClickListener {
            binding.rvPlayList.layoutManager = GridLayoutManager(context, 2)
            adapter.setLayoutType()
            binding.ivChangeGrid.visibility = View.INVISIBLE
            binding.ivChangeLinear.visibility = View.VISIBLE
        }
        binding.ivChangeLinear.setOnClickListener {
            binding.rvPlayList.layoutManager = LinearLayoutManager(context)
            adapter.setLayoutType()
            binding.ivChangeGrid.visibility = View.VISIBLE
            binding.ivChangeLinear.visibility = View.INVISIBLE
        }
        binding.ivSortItem.setOnClickListener {
            adapter.setEdit()
            setupItemTouchHelper()
            binding.ivSortItem.visibility = View.INVISIBLE
            binding.ivSortItemSave.visibility = View.VISIBLE
            if (adapter.isLinear) {
                binding.ivChangeGrid.visibility = View.INVISIBLE
            } else {
                binding.ivChangeLinear.visibility = View.INVISIBLE
            }
        }
        binding.ivSortItemSave.setOnClickListener {
            adapter.setEdit()
            setupItemTouchHelper()
            playlistDetailViewModel.updateListSong(
                playlistDetailViewModel.playlistId.value!!,
                listSongs
            )
            binding.ivSortItem.visibility = View.VISIBLE
            binding.ivSortItemSave.visibility = View.INVISIBLE
            if (adapter.isLinear) {
                binding.ivChangeGrid.visibility = View.VISIBLE
            } else {
                binding.ivChangeLinear.visibility = View.VISIBLE
            }
        }
        playlistDetailViewModel.listSong.observe(viewLifecycleOwner) {
            listSongs.removeAll({ true })
            listSongs.addAll(it)
            adapter.updateData(listSongs)
        }

        mainViewModel.song.observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setSongSelected(it)
            }
            else{
                adapter.setSongSelected(null)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}