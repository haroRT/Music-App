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
import com.example.haonv.databinding.ActivitySettingBinding
import com.example.haonv.databinding.DialogSettingLanguageBinding
import com.example.haonv.ui.auth.signin.SigninActivity
import com.example.haonv.ui.main.MainActivity
import com.example.haonv.utils.Languages
import java.util.Locale

class SettingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding
    private val settingViewModel: SettingViewModel by viewModels ()
    private fun changeLanguage(languageCode: String) {
        SharedPref.language = languageCode

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCurrentLanguage.text = Languages.getLanguageName(SharedPref.language)

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.ivConfirm.setOnClickListener {
            SharedPref.language = settingViewModel.language.value!!
            changeLanguage(settingViewModel.language.value!!)
        }

        binding.tvCurrentLanguage.setOnClickListener {
            showPopupSong(binding.tvCurrentLanguage)
        }

        settingViewModel.language.observe(this, {
            if(it != SharedPref.language){
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