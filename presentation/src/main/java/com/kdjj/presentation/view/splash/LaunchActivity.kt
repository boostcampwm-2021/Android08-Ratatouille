package com.kdjj.presentation.view.splash

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.os.postDelayed
import com.kdjj.presentation.R
import com.kdjj.presentation.common.ALPHA
import com.kdjj.presentation.common.ANIMATION_DURATION
import com.kdjj.presentation.common.SPLASH_TIME
import com.kdjj.presentation.databinding.ActivitySplashBinding
import com.kdjj.presentation.view.home.HomeActivity

class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitleAnimation()
        setFoodAnimation()
        Handler(mainLooper).postDelayed(2000) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun setTitleAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewSplashTitle, View.TRANSLATION_Y, -200f, 0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
        setAlphaAnimation(binding.imageViewSplashTitle)
    }

    private fun setFoodAnimation() {
        ObjectAnimator.ofFloat(binding.imageViewSplashFood, View.TRANSLATION_Y, 200f, 0f).apply {
            duration = ANIMATION_DURATION
            start()
        }
        setAlphaAnimation(binding.imageViewSplashFood)
    }

    private fun setAlphaAnimation(view: View) {
        ObjectAnimator.ofFloat(view, ALPHA, 0.0f, 1f).apply {
            duration = ANIMATION_DURATION
            start()
        }
    }
}