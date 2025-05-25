package com.example.haonv.ui.home.detail.album

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haonv.adapter.AlbumAdapter
import com.example.haonv.databinding.ActivityTopAlbumBinding

class TopAlbumActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopAlbumBinding
    private val topAlbumViewModel: TopAlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopAlbumBinding.inflate(layoutInflater)
        setContentView(binding.root)

        topAlbumViewModel.loadAlbums()

        binding.ivBack.setOnClickListener {
            finish()
        }

        topAlbumViewModel.albums.observe(this, {
            val albumAdapter = AlbumAdapter(it, onClick = {

            })
            binding.rvAllAlbum.adapter = albumAdapter
            binding.rvAllAlbum.layoutManager = LinearLayoutManager(this)
        })
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}