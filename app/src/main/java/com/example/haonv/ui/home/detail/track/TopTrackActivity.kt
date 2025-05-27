package com.example.haonv.ui.home.detail.track

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.haonv.adapter.TrackAdapter
import com.example.haonv.base.BaseActivity
import com.example.haonv.databinding.ActivityTopTrackBinding

class TopTrackActivity : BaseActivity<ActivityTopTrackBinding>() {
    private val topAlbumViewModel: TopTrackViewModel by viewModels()

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityTopTrackBinding {
        return ActivityTopTrackBinding.inflate(layoutInflater)
    }

    override fun setupUI() {
        super.setupUI()

    }

    override fun setupData() {
        super.setupData()

        topAlbumViewModel.loadTracks()
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        topAlbumViewModel.tracks.observe(this, {
            val trackAdapter = TrackAdapter(it)
            binding.rvAllTrack.adapter = trackAdapter
            binding.rvAllTrack.layoutManager = GridLayoutManager(this, 2)
        })
    }
}