package com.example.haonv.ui.home.detail.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haonv.adapter.AlbumAdapter
import com.example.haonv.base.BaseActivity
import com.example.haonv.databinding.ActivityTopAlbumBinding

class TopAlbumActivity : BaseActivity<ActivityTopAlbumBinding>() {
    private val topAlbumViewModel: TopAlbumViewModel by viewModels()

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityTopAlbumBinding {
        return ActivityTopAlbumBinding.inflate(layoutInflater)
    }

    override fun setupData() {
        super.setupData()

        topAlbumViewModel.loadAlbums()
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        topAlbumViewModel.albums.observe(this, {
            val albumAdapter = AlbumAdapter(it, onClick = {

            })
            binding.rvAllAlbum.adapter = albumAdapter
            binding.rvAllAlbum.layoutManager = LinearLayoutManager(this)
        })
    }
}