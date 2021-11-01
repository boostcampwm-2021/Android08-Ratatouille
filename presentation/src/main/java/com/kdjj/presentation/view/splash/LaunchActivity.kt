package com.kdjj.presentation.view.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kdjj.presentation.R
import com.kdjj.presentation.di.PresentationComponentProvider

class LaunchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        inject()
    }

    private fun inject() {
        (applicationContext as PresentationComponentProvider).providePresentationComponent().inject(this)
    }
}