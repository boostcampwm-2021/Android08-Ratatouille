package com.kdjj.presentation.view.splash

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.os.postDelayed
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
        Handler(mainLooper).postDelayed(SPLASH_TIME) {
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

    companion object {
        const val ANIMATION_DURATION = 800L
        const val SPLASH_TIME = 1500L
        const val ALPHA = "alpha"
    }
}