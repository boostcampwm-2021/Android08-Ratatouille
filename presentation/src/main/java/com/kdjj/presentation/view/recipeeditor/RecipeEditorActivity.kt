package com.kdjj.presentation.view.recipeeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ActivityRecipeEditorBinding

class RecipeEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_editor)

        setSupportActionBar(binding.toolbarEditor)
        setTitle(R.string.addRecipe)
    }
}