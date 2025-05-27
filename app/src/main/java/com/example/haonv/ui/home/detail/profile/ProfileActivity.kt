package com.example.haonv.ui.home.detail.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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
import com.example.haonv.base.BaseActivity
import com.example.haonv.databinding.ActivityProfileBinding
import com.example.haonv.databinding.ActivityTopArtistBinding
import com.example.haonv.di.DIContainer.viewModelContainer
import com.example.haonv.ui.auth.signin.SigninActivity
import com.example.haonv.ui.home.detail.artist.TopArtistViewModel
import com.example.haonv.ui.playlist.PlaylistViewModel

class ProfileActivity : BaseActivity<ActivityProfileBinding>() {
    private val profileViewModel: ProfileViewModel by lazy {
        viewModelContainer.getViewModel(
            this,
            ProfileViewModel::class.java
        )
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

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivityProfileBinding {
        return ActivityProfileBinding.inflate(layoutInflater)
    }

    override fun setupUI() {
        super.setupUI()
    }

    override fun setupData() {
        super.setupData()

        profileViewModel.loadProfile()
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivPickImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.ivConfirm.setOnClickListener {
            profileViewModel.uriImage.value?.let {
                profileViewModel.updateImageUrl(it.toString())
            }
            profileViewModel.setIsChanged(false)
        }

        binding.tvLogout.setOnClickListener {
            profileViewModel.clearUserId()
            val intent = Intent(this, SigninActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    override fun setupObserver() {
        super.setupObserver()

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

