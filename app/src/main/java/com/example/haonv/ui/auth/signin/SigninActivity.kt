package com.example.haonv.ui.auth.signin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.haonv.App
import com.example.haonv.R
import com.example.haonv.SharedPref
import com.example.haonv.base.BaseActivity
import com.example.haonv.databinding.ActivitySigninBinding
import com.example.haonv.di.DIContainer.viewModelContainer
import com.example.haonv.di.ViewModelContainer
import com.example.haonv.ui.auth.signup.SignupActivity
import com.example.haonv.ui.home.HomeViewModel
import com.example.haonv.ui.main.MainActivity


class SigninActivity : BaseActivity<ActivitySigninBinding>() {

    override fun inflateBinding(layoutInflater: LayoutInflater): ActivitySigninBinding {
        return ActivitySigninBinding.inflate(layoutInflater)
    }

    private val signinViewModel: SigninViewModel by lazy {
        viewModelContainer.getViewModel(
            this,
            SigninViewModel::class.java
        )
    }

    override fun setupUI() {
        super.setupUI()
        if (!checkPermissions()) {
            requestPermissions()
        }
        if (signinViewModel.getSharedPref().userName != "" && signinViewModel.getSharedPref().password != ""){
            binding.edtSignInUsername.setText(signinViewModel.getSharedPref().userName)
            binding.edtSignInPassword.setText(signinViewModel.getSharedPref().password)
        }
    }

    override fun setupListener() {
        super.setupListener()

        binding.edtSignInUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                signinViewModel.setDefaultValidationUsernameMessage()
            }
        })

        binding.edtSignInPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                signinViewModel.setDefaultValidationPasswordMessage()
            }
        })

        binding.txtToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnSignin.setOnClickListener {
            signinViewModel.checkLogin(
                binding.edtSignInUsername.text.toString(),
                binding.edtSignInPassword.text.toString()
            )
        }
        binding.chkRememberMe.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked == true){
                signinViewModel.getSharedPref().userName = binding.edtSignInUsername.text.toString()
                signinViewModel.getSharedPref().password = binding.edtSignInPassword.text.toString()
            }
        }

        binding.edtSignInPassword.apply {
            setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                        signinViewModel.togglePasswordVisibility()
                        view.performClick()
                        return@setOnTouchListener true
                    }
                }
                return@setOnTouchListener false
            }
            setOnClickListener {
            }
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        signinViewModel.user.observe(this){}

        signinViewModel.validationMessage.observe(this,{
            binding.txtValidateSignInUsername.text = it
        })

        signinViewModel.validationPasswordMessage.observe(this,{
            binding.txtValidateSignInPassword.text = it
        })


        signinViewModel.loginState.observe(this,{
            if (it){
                startActivity(Intent(this, MainActivity::class.java))
            }
        })

        signinViewModel.passwordVisible.observe(this) { isVisible ->
            binding.edtSignInPassword.apply {
                transformationMethod = if (isVisible) {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_auth_email,
                        0,
                        R.drawable.ic_auth_password_visible,
                        0
                    )
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_auth_email,
                        0,
                        R.drawable.ic_auth_password_invisible,
                        0
                    )
                    PasswordTransformationMethod.getInstance()
                }
                setSelection(text.length)
            }
        }
    }

    private val PERMISSIONS_REQUEST_CODE = 100

    private fun requestPermissions() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_MEDIA_AUDIO,
                        Manifest.permission.POST_NOTIFICATIONS,
                        Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
                    ),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.POST_NOTIFICATIONS
                    ),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            else -> {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    PERMISSIONS_REQUEST_CODE
                )
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                checkSinglePermission(Manifest.permission.READ_MEDIA_AUDIO) &&
                        checkSinglePermission(Manifest.permission.POST_NOTIFICATIONS) &&
                        checkSinglePermission(Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK)
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                checkSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            else -> {
                checkSinglePermission(Manifest.permission.READ_EXTERNAL_STORAGE) &&
                        checkSinglePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun checkSinglePermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            } else {
                Toast.makeText(this, "Ứng dụng cần các quyền này để hoạt động", Toast.LENGTH_LONG).show()
                openSettings()
            }
        }
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}
