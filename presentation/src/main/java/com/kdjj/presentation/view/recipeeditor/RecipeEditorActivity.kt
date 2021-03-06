package com.kdjj.presentation.view.recipeeditor

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.databinding.ActivityRecipeEditorBinding
import com.kdjj.presentation.view.adapter.RecipeEditorListAdapter
import com.kdjj.presentation.view.bindingadapter.moveTo
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.view.dialog.CustomProgressDialog
import com.kdjj.presentation.viewmodel.recipeeditor.RecipeEditorViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RecipeEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeEditorBinding

    private val viewModel: RecipeEditorViewModel by viewModels()

    private lateinit var loadingDialog: CustomProgressDialog

    private var lastCameraFileUri: String? = null
    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            lastCameraFileUri?.let { uri ->
                viewModel.setImage(uri)
            } ?: viewModel.cancelSelectImage()
        } else {
            viewModel.cancelSelectImage()
        }
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result?.data?.dataString?.let { uri ->
                viewModel.setImage(uri)
            } ?: viewModel.cancelSelectImage()
        } else {
            viewModel.cancelSelectImage()
        }
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPos = viewHolder.absoluteAdapterPosition
            val toPos = target.absoluteAdapterPosition
            if (target.itemViewType != RecipeEditorListAdapter.TYPE_STEP) return false
            viewModel.changeRecipeStepPosition(fromPos, toPos)
            return true
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            return if (viewHolder.itemViewType != RecipeEditorListAdapter.TYPE_STEP ||
                recyclerView.adapter?.itemCount == 3
            ) {
                makeMovementFlags(0, 0)
            } else {
                makeMovementFlags(
                    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                )
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.removeRecipeStep(viewHolder.absoluteAdapterPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_editor)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        loadingDialog = CustomProgressDialog(this)

        setSupportActionBar(binding.toolbarEditor)

        val adapter = RecipeEditorListAdapter(viewModel)
        binding.recyclerViewEditor.adapter = adapter
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.recyclerViewEditor)

        setupObservers()

        loadRecipe()
    }

    private fun loadRecipe() {
        val recipeId = intent.extras?.getString(RECIPE_ID)
        viewModel.initializeWith(recipeId)
    }

    private fun setupObservers() {

        viewModel.liveEditing.observe(this) { isEditing ->
            setTitle(if (isEditing) R.string.editRecipe else R.string.addRecipe)
        }

        viewModel.liveImgTarget.observe(this) { model ->
            model?.let { showSelectImageDialog() }
        }

        viewModel.liveLoading.observe(this) { doLoading ->
            if (doLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        viewModel.eventRecipeEditor.observe(this, EventObserver { event ->
            when(event){
                is RecipeEditorViewModel.RecipeEditorEvent.SaveResult -> {
                    if (event.isSuccess) {
                        ConfirmDialogBuilder.create(
                            this,
                            getString(R.string.saveFinish),
                            getString(R.string.saveFinishContent)
                        ) {
                            finish()
                        }
                    } else {
                        ConfirmDialogBuilder.create(
                            this,
                            getString(R.string.saveFailed),
                            getString(R.string.saveFailedContent)
                        ) { }
                    }
                }
                is RecipeEditorViewModel.RecipeEditorEvent.Error -> {
                    ConfirmDialogBuilder.create(
                        this,
                        getString(R.string.errorOccurs),
                        getString(R.string.ratatouilleErrorMassage)
                    ) {
                        finish()
                    }
                }
                is RecipeEditorViewModel.RecipeEditorEvent.TempDialog -> {
                    ConfirmDialogBuilder.create(
                        this,
                        getString(R.string.tempRecipe),
                        getString(R.string.tempRecipeContent),
                        true,
                        {
                            viewModel.showRecipeFromLocal(event.recipeId)
                        }
                    ) {
                        viewModel.showRecipeFromTemp()
                    }
                }
                is RecipeEditorViewModel.RecipeEditorEvent.ExitDialog -> {
                    ConfirmDialogBuilder.create(
                        this,
                        getString(R.string.exit),
                        getString(R.string.exitContent),
                        true,
                    ) {
                        viewModel.deleteTemp(true)
                    }
                }
                is RecipeEditorViewModel.RecipeEditorEvent.Exit -> {
                    finish()
                }
                is RecipeEditorViewModel.RecipeEditorEvent.MoveToPosition -> {
                    binding.recyclerViewEditor.moveTo(event.idx)
                }
            }
        })
    }

    private fun showSelectImageDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.selectPhoto)
            .setItems(R.array.photoSourceSelection) { _, i ->
                when (i) {
                    0 -> startCamera()
                    1 -> startGallery()
                    2 -> viewModel.setImageEmpty()
                }
            }
            .setOnCancelListener {
                viewModel.cancelSelectImage()
            }
            .show()
    }

    private fun startCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
                photoFile?.also {
                    val photoUri: Uri = FileProvider.getUriForFile(
                        this,
                        "com.kdjj.ratatouille.fileprovider",
                        it
                    ).also {
                        lastCameraFileUri = it.toString()
                    }
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    cameraLauncher.launch(takePictureIntent)
                }
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.KOREA).format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: throw Exception()
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }

    private fun startGallery() {
        galleryLauncher.launch(
            Intent().apply {
                type = "image/*"
                action = Intent.ACTION_GET_CONTENT
            }
        )
    }

    override fun onBackPressed() {
        viewModel.showExitDialog()
    }
}