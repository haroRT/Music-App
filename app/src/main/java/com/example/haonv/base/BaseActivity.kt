package com.example.haonv.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type.displayCutout
import androidx.core.view.WindowInsetsControllerCompat
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T: ViewBinding>: AppCompatActivity(){
    protected lateinit var binding: T
    protected abstract fun inflateBinding(layoutInflater: LayoutInflater): T

    open val isLightStatusBar: Boolean = true
    open val isHideStatusBar: Boolean = false

    @ColorRes
    protected open val statusBarColor: Int = android.R.color.white
    protected open val isHideNavigationBar: Boolean = true

    private fun configSystemBar() {
        window.statusBarColor = ContextCompat.getColor(this, statusBarColor)
        displayCutout()
        hideSystemBar(
            hideStatusBar = isHideStatusBar,
            hideNavigationBar = isHideNavigationBar,
            isLightStatusBar = isLightStatusBar
        )
    }

    private fun hideSystemBar(
        hideStatusBar: Boolean = true,
        hideNavigationBar: Boolean = true,
        isLightStatusBar: Boolean = true,
    ) {
        val controller = WindowCompat.getInsetsController(window, window.decorView)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        if (hideStatusBar) controller.hide(WindowInsetsCompat.Type.statusBars())
        if (hideNavigationBar) controller.hide(WindowInsetsCompat.Type.navigationBars())

        controller.isAppearanceLightStatusBars = isLightStatusBar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        configSystemBar()
        setupUI()
        setupData()
        setupListener()
        setupObserver()
    }

    protected open fun setupUI() {}
    protected open fun setupData() {}
    protected open fun setupListener() {}
    protected open fun setupObserver() {}

}