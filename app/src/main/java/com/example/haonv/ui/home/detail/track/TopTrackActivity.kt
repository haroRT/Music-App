package com.example.haonv.ui.home.detail.track

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.haonv.adapter.TrackAdapter
import com.example.haonv.databinding.ActivityTopTrackBinding

class TopTrackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTopTrackBinding
    private val topAlbumViewModel: TopTrackViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTopTrackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        topAlbumViewModel.loadTracks()

        topAlbumViewModel.tracks.observe(this, {
            val trackAdapter = TrackAdapter(it)
            binding.rvAllTrack.adapter = trackAdapter
            binding.rvAllTrack.layoutManager = GridLayoutManager(this, 2)
        })

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}