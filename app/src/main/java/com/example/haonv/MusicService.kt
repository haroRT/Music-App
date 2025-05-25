package com.example.haonv

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.example.haonv.data.local.entity.Song

class MusicService : Service() {
    private val CHANNEL_ID = "music_channel"
    private val NOTIFICATION_ID = 1
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var notificationLayout: RemoteViews
    private var mediaPlayer: MediaPlayer? = null
    private var listSong: List<Song>? = null
    private var currentSong: Song? = null
    private var songCallback: OnSongChanged? = null
    private var unbindCallback: UnbindCallback? = null
    private val handler = Handler(Looper.getMainLooper())
    var isShuffleEnabled: Boolean = false
    var isRepeat: Boolean = true
    var isPlaying: Boolean = false
    var isUpdating: Boolean = false


    companion object {
        private const val REQUEST_CODE_NEXT = 1
        private const val REQUEST_CODE_PREVIOUS = 2
        private const val REQUEST_CODE_DELETE = 3
        private const val REQUEST_CODE_PLAY = 4
        private const val REQUEST_CODE_PAUSE = 5
    }

    private val updateSong = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                songCallback?.onSongChanged(
                    player.currentPosition,
                    player.duration,
                    currentSong
                )
                handler.postDelayed(this, 1000)
            }
        }
    }
    // Binder để kết nối với Activity
    private val binder = MusicBinder()

    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        songCallback = null
        return super.onUnbind(intent)
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        createNotification()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener {
            playAutoNext()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            "PLAY" -> {
                resumeMusic()
            }
            "PAUSE" -> {
                pauseMusic()
            }
            "NEXT" -> {
                playNext()
            }
            "PREVIOUS" -> {
                playPrevious()
            }
            "ACTION_STOP_SERVICE" -> {
                unbindCallback?.unbindCallback()
                stopSelf()
            }
        }

        startForeground(NOTIFICATION_ID, notificationBuilder.build())

        return START_NOT_STICKY
    }

    private fun createNotification() {
        // Tạo các intent cho các nút điều khiển
        val nextIntent = PendingIntent.getService(
            this, REQUEST_CODE_NEXT,
            Intent(this, MusicService::class.java).setAction("NEXT"),
            PendingIntent.FLAG_IMMUTABLE
        )
        val previousIntent = PendingIntent.getService(
            this, REQUEST_CODE_PREVIOUS,
            Intent(this, MusicService::class.java).setAction("PREVIOUS"),
            PendingIntent.FLAG_IMMUTABLE
        )


        val deleteIntent = PendingIntent.getService(
            this, REQUEST_CODE_DELETE,
            Intent(this, MusicService::class.java).setAction("ACTION_STOP_SERVICE"),
            PendingIntent.FLAG_IMMUTABLE
        )

        notificationLayout = RemoteViews(packageName, R.layout.notification_layout)

        notificationLayout.setOnClickPendingIntent(R.id.ivPreviousSong, previousIntent)
        notificationLayout.setOnClickPendingIntent(R.id.ivNextSong, nextIntent)
        notificationLayout.setOnClickPendingIntent(R.id.ivCloseService, deleteIntent)

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_music)
            .setCustomContentView(notificationLayout)  // Layout nhỏ
            .setCustomBigContentView(notificationLayout)
            .setOnlyAlertOnce(true)
            .setAutoCancel(false)
            .setOngoing(true)
            .setSound(null)
            .setDefaults(0)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.setShowBadge(true)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun updateNotificationContent() {
        val playIntent = PendingIntent.getService(
            this, REQUEST_CODE_PLAY,
            Intent(this, MusicService::class.java).setAction("PLAY"),
            PendingIntent.FLAG_IMMUTABLE
        )
        val pauseIntent = PendingIntent.getService(
            this, REQUEST_CODE_PAUSE,
            Intent(this, MusicService::class.java).setAction("PAUSE"),
            PendingIntent.FLAG_IMMUTABLE
        )
        currentSong?.let { song ->
            notificationLayout.setTextViewText(R.id.tvNotificationTitleSong, song.name)
            notificationLayout.setTextViewText(R.id.tvSinger, song.author)
            notificationLayout.setTextViewText(R.id.tvCurrentPosition,
                "${listSong?.indexOf(song)?.plus(1)}/${listSong?.size}")
            if (song.imageURL != "")
                notificationLayout.setImageViewUri(R.id.ivThumbnail, Uri.parse(song.imageURL))
            else
                notificationLayout.setImageViewResource(R.id.ivThumbnail, R.drawable.img_song_default)

            if (isPlaying) {
                notificationLayout.setImageViewResource(R.id.ivControl, R.drawable.ic_musicbar_notification_pause)
                notificationLayout.setOnClickPendingIntent(R.id.ivControl, pauseIntent)
            } else {
                notificationLayout.setImageViewResource(R.id.ivControl, R.drawable.ic_musicbar_notification_play)
                notificationLayout.setOnClickPendingIntent(R.id.ivControl, playIntent)
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
        }
    }

    interface UnbindCallback{
        fun unbindCallback()
    }

    interface OnSongChanged {
        fun onSongChanged(currentDuration: Int, totalDuration: Int, currentSong: Song?)
    }

    fun setSongCallback(callback: OnSongChanged?) {
        handler.removeCallbacks(updateSong)
        songCallback = callback
        handler.post(updateSong)
    }

    fun setUnbindCallback(callback: UnbindCallback){
        unbindCallback = callback
    }

    fun setSong(song: Song) {
        currentSong = song
        mediaPlayer?.stop()
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(song.mp3Data)
        mediaPlayer?.prepare()
        playMusic()
        updateNotificationContent()
    }



    fun getCurrentSong(): Song? {
        return currentSong
    }

    fun setListSong(listSong: List<Song>) {
        this.listSong = listSong
    }

    fun playMusic() {
        isPlaying = true
        Log.i("12312414", "3")
        mediaPlayer?.start()
        updateNotificationContent()
    }

    fun pauseMusic() {
        isPlaying = false
        mediaPlayer?.pause()
        Log.i("12312414", "4")
        updateNotificationContent()
    }

    fun resumeMusic() {
        isPlaying = true
        mediaPlayer?.start()
        Log.i("12312414", "5")
        updateNotificationContent()
    }

    fun playNext() {

        if (currentSong == listSong?.last()) {
            if (isShuffleEnabled == true) {
                shufflePlaylist()
            }
            currentSong = listSong?.first()
            currentSong?.let {
                setSong(it)
            }
        } else {

            val currentIndex = listSong?.indexOf(currentSong)
            Log.i("12312414", "$currentIndex, ${listSong?.toString()}")
            currentSong = listSong?.get(currentIndex!! + 1)
            currentSong?.let {
                setSong(it)
            }
        }
    }

    fun playPrevious() {
        if (currentSong == listSong?.first()) {
            if (isShuffleEnabled == true) {
                shufflePlaylist()
            }
            currentSong = listSong?.last()
            currentSong?.let {
                setSong(it)
            }
        } else {

            val currentIndex = listSong?.indexOf(currentSong)
            Log.i("12312414", "$currentIndex, ${listSong?.toString()}")
            currentSong = listSong?.get(currentIndex!! -1)
            currentSong?.let {
                setSong(it)
            }
        }
    }

    fun playAutoNext() {
        Log.i("12312414", "6")
        if (currentSong == listSong?.last()) {
            if (isShuffleEnabled == true) {
                shufflePlaylist()
            }
            if (isRepeat == true) {
                playNext()
            }
        } else {
            playNext()
        }
    }


    fun setShuffle(value: Boolean) {
        isShuffleEnabled = value
    }

    fun setIsRepeat(value: Boolean) {
        isRepeat = value
    }

    fun setCurrentSong(song: Song) {
        currentSong = song
    }

    fun shufflePlaylist() {
        listSong = listSong?.shuffled()
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateSong)
        mediaPlayer?.release()
        mediaPlayer = null
        isUpdating = false
    }
}