package com.example.haonv.ui.home.detail.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.example.haonv.App
import com.example.haonv.R
import com.example.haonv.SharedPref
import com.example.haonv.adapter.ArtistAdapter
import com.example.haonv.databinding.ActivityProfileBinding
import com.example.haonv.databinding.ActivityTopArtistBinding
import com.example.haonv.ui.auth.signin.SigninActivity
import com.example.haonv.ui.home.detail.artist.TopArtistViewModel
import com.example.haonv.ui.playlist.PlaylistViewModel

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        ProfileViewModel.ProfileViewModelFactory(application as App)
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            contentResolver.takePersistableUriPermission(
                uri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            profileViewModel.setUriImage(uri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.ivConfirm.setOnClickListener {
            profileViewModel.uriImage.value?.let {
                profileViewModel.updateImageUrl(SharedPref.userId, it.toString())
            }
            profileViewModel.setIsChanged(false)
        }

        binding.tvLogout.setOnClickListener {
            SharedPref.clearUserId()
            val intent = Intent(this, SigninActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        profileViewModel.loadProfile(SharedPref.userId)

        profileViewModel.user.observe(this, {
            Log.i("use3123r", it.toString())
            if (it != null) {
                Glide.with(this)
                    .load(it.imageUrl)
                    .downsample(DownsampleStrategy.AT_MOST)
                    .error(R.drawable.img_song_default)
                    .centerCrop()
                    .placeholder(R.drawable.img_song_default)
                    .into(binding.ivAvatar)
                binding.tvUsername.text = it.username
                binding.tvMail.text = it.email
            }
        })

        profileViewModel.uriImage.observe(this, {
            profileViewModel.setIsChanged(true)
            Glide.with(this)
                .load(it)
                .downsample(DownsampleStrategy.AT_MOST)
                .error(R.drawable.img_song_default)
                .centerCrop()
                .placeholder(R.drawable.img_song_default)
                .into(binding.ivAvatar)
        })

        profileViewModel.isChanged.observe(this, {
            if (it){
                binding.ivConfirm.visibility = View.VISIBLE
            }
            else{
                binding.ivConfirm.visibility = View.INVISIBLE
            }
        })

    }

}

