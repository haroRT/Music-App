package com.example.haonv.ui.player

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.haonv.MusicService
import com.example.haonv.R
import com.example.haonv.base.BaseActivity
import com.example.haonv.data.local.entity.Song
import com.example.haonv.databinding.ActivityPlayerBinding
import com.example.haonv.utils.formatDuration
import kotlinx.coroutines.launch

class PlayerActivity : BaseActivity<ActivityPlayerBinding>(), MusicService.OnSongChanged {
    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityPlayerBinding {
        return ActivityPlayerBinding.inflate(layoutInflater)
    }
    private var musicService: MusicService? = null
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            musicService?.setSongCallback(this@PlayerActivity)
            if (musicService?.isPlaying == true){
                playerViewModel.setIsPlaying(true)
            }
            else{
                playerViewModel.setIsPlaying(false)
            }
            playerViewModel.setIsBound(true)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            playerViewModel.setIsBound(false)
        }
    }

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onSongChanged(currentDuration: Int, totalDuration: Int, currentSong: Song?) {
        currentSong?.let {
            if (playerViewModel.song.value == null || playerViewModel.song.value != currentSong) {
                playerViewModel.setSong(currentSong)
            }
            binding.tvDurationSong.text = currentSong.duration.formatDuration()
            binding.tvDurationNow.text = currentDuration.toLong().formatDuration()
            binding.sbPlayer.max = totalDuration
            binding.sbPlayer.progress = currentDuration
        }
    }

    private fun isServiceRunning(): Boolean {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications
        return notifications.any { it.id == 1 }
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBack.setOnClickListener{
            finish()
        }
        binding.ivDelete.setOnClickListener{
            if (playerViewModel.isBound.value == true) {
                unbindService(connection)
                stopService(Intent(this, MusicService::class.java))
                playerViewModel.setIsBound(false)
            }
            finish()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        playerViewModel.song.observe(this, {
            if (it != null) {
                Glide.with(this)
                    .load(it.imageURL)
                    .centerCrop()
                    .error(R.drawable.img_song_default)
                    .into(binding.ivImageSong)
                binding.tvTitleSong.text = it.name
                binding.tvSinger.text = it.author
            }
        })

        playerViewModel.isBound.observe(this, {
            if (it) {
                binding.ivNext.setOnClickListener{
                    musicService?.playNext()
                    playerViewModel.setIsPlaying(true)
                }
                binding.ivPrevious.setOnClickListener{
                    musicService?.playPrevious()
                    playerViewModel.setIsPlaying(true)
                }
                binding.ivPlay.setOnClickListener{
                    if(playerViewModel.isPlaying.value == true){
                        musicService?.pauseMusic()
                        playerViewModel.setIsPlaying(false)
                    }
                    else{
                        musicService?.resumeMusic()
                        playerViewModel.setIsPlaying(true)
                    }
                }
            }
        })

        playerViewModel.isPlaying.observe(this, {
            if (it){
                binding.ivPlay.setImageResource(R.drawable.ic_musicbar_notification_pause)
            }
            else{
                binding.ivPlay.setImageResource(R.drawable.ic_player_continue)
            }
        })

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                if (isServiceRunning()) {
                    if (playerViewModel.isBound.value == false) {
                        val intent = Intent(this@PlayerActivity, MusicService::class.java)
                        bindService(intent, connection, Context.BIND_AUTO_CREATE)
                        playerViewModel.setIsBound(true)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStop() {
        super.onStop()
        if (playerViewModel.isBound.value == true) {
            unbindService(connection)
            playerViewModel.setIsBound(false)
        }
    }
}