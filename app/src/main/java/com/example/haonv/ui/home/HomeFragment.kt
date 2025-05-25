package com.example.haonv.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.example.haonv.App
import com.example.haonv.R
import com.example.haonv.SharedPref
import com.example.haonv.adapter.AlbumAdapter
import com.example.haonv.adapter.ArtistAdapter
import com.example.haonv.adapter.TrackAdapter
import com.example.haonv.data.remote.response.Album
import com.example.haonv.databinding.FragmentHomeBinding
import com.example.haonv.ui.home.detail.album.TopAlbumActivity
import com.example.haonv.ui.home.detail.artist.TopArtistActivity
import com.example.haonv.ui.home.detail.artist.TopArtistViewModel
import com.example.haonv.ui.home.detail.profile.ProfileActivity
import com.example.haonv.ui.home.detail.setting.SettingActivity
import com.example.haonv.ui.home.detail.track.TopTrackActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels{
        HomeViewModel.HomeViewModelFactory(requireActivity().application as App)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.loadAllData()

        homeViewModel.loadProfile(SharedPref.userId)

        binding.tvTopAlbumShowMore.setOnClickListener {
            startActivity(Intent(context, TopAlbumActivity::class.java))
        }

        binding.tvTopTrackShowMore.setOnClickListener {
            startActivity(Intent(context, TopTrackActivity::class.java))
        }

        binding.tvTopArtistShowMore.setOnClickListener {
            startActivity(Intent(context, TopArtistActivity::class.java))
        }

        binding.cvAvatar.setOnClickListener{
            startActivity(Intent(context, ProfileActivity::class.java))
        }

        binding.ivSetting.setOnClickListener{
            startActivity(Intent(context, SettingActivity::class.java))
        }

        binding.btnTryAgain.setOnClickListener {
            homeViewModel.loadAllData()
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.svHome.visibility = View.INVISIBLE
                binding.lavLoading.visibility = View.VISIBLE
            } else {
                binding.lavLoading.visibility = View.INVISIBLE
                binding.svHome.visibility = View.VISIBLE
            }
        }

        homeViewModel.isErrorNetwork.observe(viewLifecycleOwner,{
            if (it){
                binding.lavLoading.visibility = View.INVISIBLE
                binding.llErrorNetwork.visibility = View.VISIBLE
                binding.cslShow.visibility = View.INVISIBLE
            }
            else{
                binding.llErrorNetwork.visibility = View.INVISIBLE
                binding.cslShow.visibility = View.VISIBLE
            }
        })

        homeViewModel.albums.observe(viewLifecycleOwner,{
            val albumAdapter = AlbumAdapter(dataset = it, onClick = {
                album ->  Log.i("album", "album: $album")
            })
            albumAdapter.setLayoutType()
            binding.rvTopAlbum.layoutManager = GridLayoutManager(context, 2)
            binding.rvTopAlbum.adapter = albumAdapter
        })

        homeViewModel.tracks.observe(viewLifecycleOwner, {
            val trackAdapter = TrackAdapter(it)
            binding.rvTopTrack.adapter = trackAdapter
            binding.rvTopTrack.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })

        homeViewModel.artists.observe(viewLifecycleOwner,{
            val artistAdapter = ArtistAdapter(it)
            binding.rvTopArtist.adapter = artistAdapter
            binding.rvTopArtist.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        })

        homeViewModel.user.observe(viewLifecycleOwner,{
            Glide.with(this)
                .load(it.imageUrl)
                .downsample(DownsampleStrategy.AT_MOST)
                .error(R.drawable.img_song_default)
                .centerCrop()
                .placeholder(R.drawable.img_song_default)
                .into(binding.ivAvatar)
            binding.tvUsername.text = it.username
        })
    }
}