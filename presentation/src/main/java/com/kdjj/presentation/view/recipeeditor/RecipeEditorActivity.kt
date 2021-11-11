package com.kdjj.presentation.view.recipeeditor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ActivityRecipeEditorBinding
import com.kdjj.presentation.model.RecipeEditorItem
import com.kdjj.presentation.view.adapter.RecipeEditorListAdapter
import com.kdjj.presentation.viewmodel.recipeeditor.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeEditorBinding
    private val viewModel: RecipeEditorViewModel by viewModels()

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            viewModel.setImage(result?.data?.dataString)
        } else {
            viewModel.setImage(null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_editor)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setSupportActionBar(binding.toolbarEditor)
        setTitle(R.string.addRecipe)

        val adapter = RecipeEditorListAdapter(viewModel)
        binding.recyclerViewEditor.adapter = adapter

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.liveImgTarget.observe(this) { model ->
            model?.let { showSelectImageDialog() }
        }
    }

    private fun showSelectImageDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.selectPhoto)
            .setItems(R.array.photoSourceSelection) { _, i ->
                when (i) {
                    0 -> {
                    }
                    1 -> {
                        galleryLauncher.launch(
                            Intent().apply {
                                type = "image/*"
                                action = Intent.ACTION_GET_CONTENT
                            }
                        )
                    }
                    2 -> {
                        viewModel.setImageEmpty()
                    }
                }
            }
            .show()
    }
}