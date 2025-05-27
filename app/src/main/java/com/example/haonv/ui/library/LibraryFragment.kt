package com.example.haonv.ui.library


import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haonv.App
import com.example.haonv.R
import com.example.haonv.SharedPref
import com.example.haonv.adapter.PlaylistAdapter
import com.example.haonv.adapter.SongAdapter
import com.example.haonv.base.BaseFragment
import com.example.haonv.data.local.entity.Song
import com.example.haonv.databinding.DialogAddSongToPlaylistBinding
import com.example.haonv.databinding.DialogSelectPlaylistBinding
import com.example.haonv.databinding.FragmentLibraryBinding
import com.example.haonv.di.DIContainer.viewModelContainer
import com.example.haonv.ui.main.MainViewModel
import com.example.haonv.ui.playlist.PlaylistFragment

class LibraryFragment : BaseFragment<FragmentLibraryBinding>() {

    private val libraryViewModel: LibraryViewModel by lazy {
        viewModelContainer.getViewModel(
            this,
            LibraryViewModel::class.java
        )
    }
    private var currentPopupWindow: PopupWindow? = null
    private lateinit var adapter: SongAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLibraryBinding {
        return FragmentLibraryBinding.inflate(inflater, container, false)
    }

    override fun setupUI() {
        super.setupUI()

        adapter = SongAdapter(mutableListOf())
        binding.rvLibrary.adapter = adapter
        binding.rvLibrary.layoutManager = LinearLayoutManager(context)
    }

    override fun setupData() {
        super.setupData()
        libraryViewModel.setIsLocal(true)
    }

    override fun setupListener() {
        super.setupListener()

        binding.btnLocal.setOnClickListener {
            libraryViewModel.setIsLocal(true)
        }
        binding.btnRemote.setOnClickListener {
            libraryViewModel.setIsLocal(false)
        }

        binding.btnTryAgain.setOnClickListener {
            libraryViewModel.setIsErrorNetwork(false)
            libraryViewModel.getListSongFromRemote()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        libraryViewModel.songs.observe(viewLifecycleOwner) {
            adapter = SongAdapter(it)
            if (libraryViewModel.song.value != null){
                adapter.setSongSelected(libraryViewModel.song.value!!)
            }
            binding.rvLibrary.layoutManager = LinearLayoutManager(context)
            adapter.setOnItemMoreOptionClickListener(object :
                SongAdapter.OnItemMoreOptionClickListener {
                override fun onItemMoreOptionClick(view: View, position: Int, song: Song) {
                    showPopupSong(view, position, song)
                }
            })
            adapter.setOnItemClickListener(object : SongAdapter.OnItemClickListener {
                override fun onItemClick(song: Song, listSong: List<Song>) {
                    mainViewModel.setSong(song)
                    mainViewModel.setListSong(listSong)
                }
            })
            binding.rvLibrary.adapter = adapter
        }

        mainViewModel.song.observe(viewLifecycleOwner, {
            if(it != null){
                libraryViewModel.setSong(it)
                adapter.setSongSelected(it)
            }
            else{
                libraryViewModel.setSong(null)
                adapter.setSongSelected(null)
            }
        })

        libraryViewModel.isLocal.observe(viewLifecycleOwner) {
            if (it) {
                binding.btnLocal.setBackgroundColor(
                    resources.getColor(
                        R.color.library_btn_select,
                        null
                    )
                )
                binding.btnRemote.setBackgroundColor(
                    resources.getColor(
                        R.color.library_btn_unselect,
                        null
                    )
                )
                libraryViewModel.setIsLoading(false)
                libraryViewModel.getListSongsFromLocal()

            } else {
                binding.btnLocal.setBackgroundColor(
                    resources.getColor(
                        R.color.library_btn_unselect,
                        null
                    )
                )
                binding.btnRemote.setBackgroundColor(
                    resources.getColor(
                        R.color.library_btn_select,
                        null
                    )
                )

                libraryViewModel.getListSongFromRemote()
            }
        }

        libraryViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.lavLoading.visibility = View.VISIBLE
                binding.rvLibrary.visibility = View.INVISIBLE

            } else {
                binding.lavLoading.visibility = View.INVISIBLE
                binding.rvLibrary.visibility = View.VISIBLE
            }
        }

        libraryViewModel.isErrorNetwork.observe(viewLifecycleOwner) {
            if (it) {
                binding.llErrorNetwork.visibility = View.VISIBLE
                binding.rvLibrary.visibility = View.INVISIBLE
            } else {
                binding.llErrorNetwork.visibility = View.INVISIBLE
                binding.rvLibrary.visibility = View.VISIBLE
            }
        }
    }

    fun showPopupSong(view: View, position: Int, song: Song) {

        val binding = DialogAddSongToPlaylistBinding.inflate(LayoutInflater.from(requireContext()))

        val popupWindow = PopupWindow(
            binding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        binding.tvAddToPlaylist.setOnClickListener {
            showPopupSelectPlaylist(view, position, song)
            popupWindow.dismiss()
        }
        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = true
        }
        currentPopupWindow?.dismiss()
        currentPopupWindow = popupWindow
        popupWindow.showAsDropDown(
            view,
            view.width - 600,
            12
        )
    }


    fun showPopupSelectPlaylist(view: View, position: Int, song: Song) {

        val binding = DialogSelectPlaylistBinding.inflate(LayoutInflater.from(requireContext()))

        val popupWindow = PopupWindow(
            binding.root,
            1000,
            1200
        )

        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = true
        }
        libraryViewModel.getPlaylistByUserId()
        libraryViewModel.playlists.observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                binding.grAddPlaylist.visibility = View.VISIBLE
                binding.rvPlayList.visibility = View.INVISIBLE
            } else {
                binding.grAddPlaylist.visibility = View.INVISIBLE
                binding.rvPlayList.visibility = View.VISIBLE
            }
        })
        libraryViewModel.playlists.observe(viewLifecycleOwner, {
            val adapter = PlaylistAdapter(it)
            adapter.setItemSelect(true)
            adapter.setOnItemClickListener(object : PlaylistAdapter.OnItemClickListener {
                override fun onItemClick(id: Int) {
                    libraryViewModel.addSongToPlaylist(id, song.id, song)
                    popupWindow.dismiss()
                }
            })
            binding.rvPlayList.layoutManager = LinearLayoutManager(context)
            binding.rvPlayList.adapter = adapter
        })

        binding.addPlaylistButton.setOnClickListener {
            val playlistFragment = PlaylistFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.llMain, playlistFragment)
                .addToBackStack(null)
                .commit()
            playlistFragment.arguments = Bundle().apply {
                putBoolean("SHOW_POPUP", true)
            }
        }

        currentPopupWindow?.dismiss()
        currentPopupWindow = popupWindow
        popupWindow.showAtLocation(binding.root, Gravity.CENTER, 0, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentPopupWindow?.dismiss()
        currentPopupWindow = null
    }

}