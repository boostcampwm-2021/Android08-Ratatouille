package com.kdjj.presentation.view.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kdjj.presentation.R
import com.kdjj.presentation.view.home.HomeActivity
import com.kdjj.presentation.view.recipeeditor.RecipeEditorActivity

class LaunchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startActivity(Intent(this, HomeActivity::class.java))
    }
}