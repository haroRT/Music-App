package com.example.haonv.ui.home.detail.artist

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haonv.adapter.ArtistAdapter
import com.example.haonv.databinding.ActivityTopArtistBinding


class TopArtistActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopArtistBinding
    private val topArtistViewModel: TopArtistViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopArtistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        topArtistViewModel.loadArtists()

        topArtistViewModel.artists.observe(this,{
            val artistAdapter = ArtistAdapter(it)
            binding.rvAllArtist.adapter = artistAdapter
            binding.rvAllArtist.layoutManager = GridLayoutManager(this, 2)
        })
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}