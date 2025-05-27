package com.example.haonv.ui.home.detail.artist

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haonv.adapter.ArtistAdapter
import com.example.haonv.base.BaseActivity
import com.example.haonv.databinding.ActivityTopArtistBinding


class TopArtistActivity : BaseActivity<ActivityTopArtistBinding>() {

    private val topArtistViewModel: TopArtistViewModel by viewModels()

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityTopArtistBinding {
        return ActivityTopArtistBinding.inflate(layoutInflater)
    }

    override fun setupData() {
        super.setupData()

        topArtistViewModel.loadArtists()
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        topArtistViewModel.artists.observe(this,{
            val artistAdapter = ArtistAdapter(it)
            binding.rvAllArtist.adapter = artistAdapter
            binding.rvAllArtist.layoutManager = GridLayoutManager(this, 2)
        })
    }
}