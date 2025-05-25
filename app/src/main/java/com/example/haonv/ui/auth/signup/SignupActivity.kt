package com.example.haonv.ui.auth.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.MotionEvent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.haonv.App
import com.example.haonv.R
import com.example.haonv.databinding.ActivitySignupBinding
import com.example.haonv.ui.auth.signin.SigninActivity


class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val signupViewModel: SignupViewModel by viewModels {
        SignupViewModel.SignupViewModelFactory(application as App)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signupViewModel.signupState.observe(this, {
            if (it) {
                startActivity(Intent(this, SigninActivity::class.java))
            }
        })

        signupViewModel.validationMessage.observe(this, {
            binding.txtValidateSignUpUsername.text = it
        })
        signupViewModel.validationPasswordMessage.observe(this, {
            binding.txtValidateSignUpPassword.text = it
        })
        signupViewModel.validationConfirmPasswordMessage.observe(this, {
            binding.txtValidateSignUpConfirmPassword.text = it
        })
        signupViewModel.validationEmailMessage.observe(this, {
            binding.txtValidateSignUpEmail.text = it
        })

        binding.edtSignUpUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                signupViewModel.setDefaultValidationUsernameMessage()
            }
        })

        binding.edtSignUpPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                signupViewModel.setDefaultValidationPasswordMessage()
            }
        })

        binding.edtSignUpConfirmPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                signupViewModel.setDefaultValidationConfirmPasswordMessage()
            }
        })
        binding.edtSignUpEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                signupViewModel.setDefaultValidationEmailMessage()
            }
        })

        binding.ivtoSignin.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignUp.setOnClickListener {
            val username = binding.edtSignUpUsername.text.toString().trim()
            val password = binding.edtSignUpPassword.text.toString().trim()
            val confirmPassword = binding.edtSignUpConfirmPassword.text.toString().trim()
            val email = binding.edtSignUpEmail.text.toString().trim()

            signupViewModel.checkSignup(username, password, email, confirmPassword, "012345678")

        }
        setupPasswordToggles()
        observePasswordVisibility()
    }

    private fun setupPasswordToggles() {
        binding.edtSignUpPassword.apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                        signupViewModel.togglePasswordVisibility()
                        view.performClick()
                        return@setOnTouchListener true
                    }
                }
                false
            }
            setOnClickListener {
            }
        }

        binding.edtSignUpConfirmPassword.apply {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    if (event.rawX >= (right - compoundDrawables[2].bounds.width())) {
                        signupViewModel.toggleConfirmPasswordVisibility()
                        view.performClick()
                        return@setOnTouchListener true
                    }
                }
                false
            }
            setOnClickListener {
            }
        }
    }

    private fun observePasswordVisibility() {
        signupViewModel.passwordVisible.observe(this) { isVisible ->
            binding.edtSignUpPassword.apply {
                transformationMethod = if (isVisible) {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_auth_password,
                        0,
                        R.drawable.ic_auth_password_visible,
                        0
                    )
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_auth_password,
                        0,
                        R.drawable.ic_auth_password_invisible,
                        0
                    )
                    PasswordTransformationMethod.getInstance()
                }
                setSelection(text.length)
            }
        }

        signupViewModel.confirmPasswordVisible.observe(this) { isVisible ->
            binding.edtSignUpConfirmPassword.apply {
                transformationMethod = if (isVisible) {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_auth_password,
                        0,
                        R.drawable.ic_auth_password_visible,
                        0
                    )
                    HideReturnsTransformationMethod.getInstance()
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_auth_password,
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

}