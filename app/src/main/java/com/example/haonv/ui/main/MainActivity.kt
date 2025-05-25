package com.example.haonv.ui.main

import android.Manifest
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.haonv.MusicService
import com.example.haonv.R
import com.example.haonv.data.local.entity.Song
import com.example.haonv.databinding.ActivityMainBinding
import com.example.haonv.ui.home.HomeFragment
import com.example.haonv.ui.library.LibraryFragment
import com.example.haonv.ui.player.PlayerActivity
import com.example.haonv.ui.playlist.PlaylistFragment
import com.example.haonv.utils.formatDuration
import java.util.Locale


class MainActivity : AppCompatActivity(), MusicService.OnSongChanged, MusicService.UnbindCallback {
    private lateinit var binding: ActivityMainBinding
    private var musicService: MusicService? = null

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MusicService.MusicBinder
            musicService = binder.getService()
            musicService?.setSongCallback(this@MainActivity)
            musicService?.setUnbindCallback(this@MainActivity)
            mainViewModel.setIsBound(true)
            val currentSong = musicService?.getCurrentSong()
            if (currentSong == null && mainViewModel.song.value != null) {
                musicService?.setSong(mainViewModel.song.value!!)
                musicService?.setListSong(mainViewModel.listSong.value!!)
                musicService?.playMusic()
                if (musicService?.isPlaying == true) {
                    mainViewModel.setIsPlaying(true)
                } else {
                    mainViewModel.setIsPlaying(false)
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            musicService = null
            mainViewModel.setIsBound(false)
        }
    }

    override fun onSongChanged(currentDuration: Int, totalDuration: Int, currentSong: Song?) {
        currentSong?.let {
            if (mainViewModel.song.value != null
                && mainViewModel.song.value != it
            ) {
                mainViewModel.setSong(it)
            }
            mainViewModel.setMusicBarState(true)
            if (musicService?.isPlaying == true) {
                mainViewModel.setIsPlaying(true)
            } else {
                mainViewModel.setIsPlaying(false)
            }
            binding.tvTitleSong.text = currentSong.name
            binding.tvDurationSong.text = currentSong.duration.formatDuration()
        }
        binding.sbMusicBar.max = totalDuration
        binding.sbMusicBar.progress = currentDuration
    }

    override fun unbindCallback() {
        if (mainViewModel.isBound.value == true) {
            unbindService(connection)
            mainViewModel.setIsBound(false)
            mainViewModel.setMusicBarState(false)
            mainViewModel.setIsPlaying(false)
            mainViewModel.setSong(null)
        }
    }

    private fun isServiceRunning(): Boolean {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifications = notificationManager.activeNotifications
        return notifications.any { it.id == 1 }
    }

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }

        binding.bnMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.nav_library -> {
                    loadFragment(LibraryFragment())
                    true
                }

                R.id.nav_playlist -> {
                    loadFragment(PlaylistFragment())
                    true
                }

                else -> false
            }
        }

        binding.ivClose.setOnClickListener {
            if (mainViewModel.isBound.value == true) {
                unbindService(connection)
                stopService(Intent(this, MusicService::class.java))
                mainViewModel.setSong(null)
                mainViewModel.setIsBound(false)
            }
            mainViewModel.setMusicBarState(false)
        }

        mainViewModel.musicBarState.observe(this, {
            if (it) {
                binding.ivClose.visibility = View.VISIBLE
                binding.cslMusicBar.visibility = View.VISIBLE
            } else {
                binding.cslMusicBar.visibility = View.GONE
                binding.ivClose.visibility = View.GONE
            }
        })

        mainViewModel.song.observe(this, {

            if (it != null) {
                mainViewModel.setMusicBarState(true)
                if (mainViewModel.isBound.value == false) {
                    startService()
                } else {
                    val currentSong = musicService?.getCurrentSong()
                    if (currentSong != null
                        && it.id != currentSong.id
                        && it.name != currentSong.name
                    ) {

                        musicService?.setSong(it)
                        musicService?.playMusic()
                    }
                    else{
                        if (musicService?.isPlaying == true) {
                            musicService?.pauseMusic()
                            mainViewModel.setIsPlaying(false)
                        } else {
                            musicService?.resumeMusic()
                            mainViewModel.setIsPlaying(true)
                        }
                    }
                }
            }
        })

        mainViewModel.isBound.observe(this, {})

        mainViewModel.listSong.observe(this, {

            if (it != null) {
                musicService?.setListSong(it)
            }
        })

        mainViewModel.isPlaying.observe(this, {
            if (it) {
                binding.ivControl.setImageResource(R.drawable.ic_musicbar_notification_pause)
            } else {
                binding.ivControl.setImageResource(R.drawable.ic_musicbar_notification_play)
            }
        })

        binding.ivControl.setOnClickListener {
            if (musicService?.isPlaying == true) {
                musicService?.pauseMusic()
                mainViewModel.setIsPlaying(false)
            } else {
                musicService?.resumeMusic()
                mainViewModel.setIsPlaying(true)
            }
        }

        binding.cslMusicBar.setOnClickListener {
            startActivity(Intent(this, PlayerActivity::class.java))
        }
    }

    override fun onStart() {
        super.onStart()
        if (isServiceRunning()) {
            if (mainViewModel.isBound.value == false) {
                val intent = Intent(this, MusicService::class.java)
                bindService(intent, connection, Context.BIND_AUTO_CREATE)
                mainViewModel.setIsBound(true)
            }
        } else {
            mainViewModel.setMusicBarState(false)
            mainViewModel.setIsBound(false)
            mainViewModel.setSong(null)
        }
        if (checkSelfPermission(Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 200)
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 200)
            }
        }
    }


    override fun onStop() {
        super.onStop()
        if (mainViewModel.isBound.value == true) {
            unbindService(connection)
            mainViewModel.setIsBound(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun startService() {
        val intent = Intent(
            this,
            MusicService::class.java
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
            startService(intent)
        }
        bindService(intent, connection, Context.BIND_AUTO_CREATE)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.llMain, fragment)
            .commit()
    }

}