package com.example.haonv.ui.home.detail.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.haonv.SharedPref
import com.example.haonv.base.BaseActivity
import com.example.haonv.databinding.ActivitySettingBinding
import com.example.haonv.databinding.DialogSettingLanguageBinding
import com.example.haonv.di.DIContainer.viewModelContainer
import com.example.haonv.ui.auth.signin.SigninActivity
import com.example.haonv.ui.main.MainActivity
import com.example.haonv.utils.Languages
import java.util.Locale

class SettingActivity : BaseActivity<ActivitySettingBinding>() {
    private val settingViewModel: SettingViewModel by lazy {
        viewModelContainer.getViewModel(
            this,
            SettingViewModel::class.java
        )
    }

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }
    
    private fun changeLanguage(languageCode: String) {
        settingViewModel.setLanguageSharedPref(languageCode)
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    fun showPopupSong(view: View) {

        val binding = DialogSettingLanguageBinding.inflate(LayoutInflater.from(this))

        val popupWindow = PopupWindow(
            binding.root,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        binding.tvEnglish.setOnClickListener {
            settingViewModel.setLanguage("en")
            popupWindow.dismiss()
        }

        binding.tvVietnamese.setOnClickListener {
            settingViewModel.setLanguage("vi")
            popupWindow.dismiss()
        }

        binding.tvKorean.setOnClickListener {
            settingViewModel.setLanguage("ko")
            popupWindow.dismiss()
        }

        binding.tvFrench.setOnClickListener {
            settingViewModel.setLanguage("fr")
            popupWindow.dismiss()
        }

        popupWindow.apply {
            isFocusable = true
            isOutsideTouchable = true
        }

        popupWindow.showAsDropDown(
            view,
            view.width - 400,
            20
        )
    }

    override fun setupUI() {
        super.setupUI()
        
        binding.tvCurrentLanguage.text = Languages.getLanguageName(settingViewModel.getLanguage())
    }
    
    override fun setupData() {
        super.setupData()
    }

    override fun setupListener() {
        super.setupListener()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivConfirm.setOnClickListener {
            settingViewModel.setLanguageSharedPref(settingViewModel.language.value!!)
            changeLanguage(settingViewModel.language.value!!)
        }

        binding.tvCurrentLanguage.setOnClickListener {
            showPopupSong(binding.tvCurrentLanguage)
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        settingViewModel.language.observe(this, {
            if(it != settingViewModel.getLanguage()){
                settingViewModel.setIsChanged(true)
            }
        })

        settingViewModel.isChanged.observe(this, {
            if(it)
                binding.ivConfirm.visibility = View.VISIBLE

            else
                binding.ivConfirm.visibility = View.INVISIBLE
        })
    }

}