package com.kdjj.presentation.view.recipeeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ActivityRecipeEditorBinding
import com.kdjj.presentation.view.adapter.RecipeEditorListAdapter
import com.kdjj.presentation.viewmodel.recipeeditor.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeEditorBinding
    private val viewModel: RecipeEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_editor)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbarEditor)
        setTitle(R.string.addRecipe)

        val adapter = RecipeEditorListAdapter(viewModel)
        binding.recyclerViewEditor.adapter = adapter
    }
}