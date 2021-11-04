package com.kdjj.presentation.view.recipeeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.kdjj.domain.model.RecipeType
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ActivityRecipeEditorBinding
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.view.adapter.RecipeEditorListAdapter

class RecipeEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeEditorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_editor)

        setSupportActionBar(binding.toolbarEditor)
        setTitle(R.string.addRecipe)

        val adapter = RecipeEditorListAdapter()
        binding.recyclerViewEditor.adapter = adapter

        adapter.submitList(listOf(
            RecipeEditorItem.RecipeMeta(
                "", RecipeType(1, "test"), "", ""
            ),
            RecipeEditorItem.AddStep
        ))
    }
}