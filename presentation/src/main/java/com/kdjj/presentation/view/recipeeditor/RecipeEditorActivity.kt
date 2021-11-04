package com.kdjj.presentation.view.recipeeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
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

        val adapter = ArrayAdapter(
            this,
            R.layout.item_editor_recipe_type,
            arrayOf("한식", "양식", "중식", "일식")
        )
        binding.spinnerEditorRecipeType.adapter = adapter
    }
}