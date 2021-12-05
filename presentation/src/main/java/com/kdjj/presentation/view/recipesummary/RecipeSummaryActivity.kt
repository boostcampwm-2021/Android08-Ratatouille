package com.kdjj.presentation.view.recipesummary

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.kdjj.domain.model.RecipeState
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.common.extensions.throttleFirst
import com.kdjj.presentation.databinding.ActivityRecipeSummaryBinding
import com.kdjj.presentation.model.RecipeSummaryType
import com.kdjj.presentation.model.UpdateFavoriteResult
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.view.dialog.CustomProgressDialog
import com.kdjj.presentation.view.recipedetail.RecipeDetailActivity
import com.kdjj.presentation.view.recipeeditor.RecipeEditorActivity
import com.kdjj.presentation.viewmodel.recipesummary.RecipeSummaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipeSummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeSummaryBinding
    private val recipeSummaryViewModel: RecipeSummaryViewModel by viewModels()
    private val floatingMenuIdListMap: Map<RecipeSummaryType, List<AppCompatButton>> by lazy {
        mapOf(
            RecipeSummaryType.MY_SAVE_RECIPE to listOf(
                binding.buttonSummaryUpload,
                binding.buttonSummaryDelete,
                binding.buttonSummaryEdit,
                binding.buttonSummaryFavorite,
            ),
            RecipeSummaryType.OTHER_SERVER_RECIPE to listOf(
                binding.buttonSummarySteal,
                binding.buttonSummaryStealFavorite,
            ),
            RecipeSummaryType.MY_SERVER_RECIPE to listOf(
                binding.buttonSummaryDelete
            ),
            RecipeSummaryType.MY_SAVE_OTHER_RECIPE to listOf(
                binding.buttonSummaryDelete,
                binding.buttonSummaryEdit,
                binding.buttonSummaryFavorite,
            )
        )
    }
    private var isInitializeFab = false

    private lateinit var loadingDialog: CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_summary)
        binding.viewModel = recipeSummaryViewModel
        binding.lifecycleOwner = this

        loadingDialog = CustomProgressDialog(this)

        setSupportActionBar(binding.toolbarSummary)

        initViewModel()
        initObservers()
        setEventObservers()
        setButtonClickObserver()
    }

    private fun initObservers() = with(recipeSummaryViewModel) {

        liveRecipe.observe(this@RecipeSummaryActivity) { recipe ->
            title = recipe.title
            isInitializeFab = false
        }

        liveLoading.observe(this@RecipeSummaryActivity) { doLoading ->
            if (doLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        liveFabState.observe(this@RecipeSummaryActivity) { (recipeSummaryType, isFabOpen) ->
            val buttonList = floatingMenuIdListMap[recipeSummaryType]
            if (!isInitializeFab) {
                binding.groupSummaryFabMenu.alpha = 0.0f
                binding.groupSummaryFabMenu.visibility = View.GONE
                isInitializeFab = true
            }
            if (isFabOpen) {
                buttonList?.forEachIndexed { index, button ->
                    button.animate()
                        .alpha(1.0f)
                        .setDuration(80L * index)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator?) {
                                button.visibility = View.VISIBLE
                            }
                        })
                }
            } else {
                buttonList?.reversed()?.forEachIndexed { index, button ->
                    button.animate()
                        .alpha(0.0f)
                        .setDuration(80L * index)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator?) {
                                button.visibility = View.GONE
                            }
                        })
                }
            }
        }
    }

    private fun setButtonClickObserver() = with(recipeSummaryViewModel) {
        lifecycleScope.launchWhenStarted {
            buttonClickFLow.throttleFirst()
                .collect {
                    when (it) {
                        is RecipeSummaryViewModel.ButtonClick.OpenRecipeDetail -> {
                            val intent = Intent(
                                this@RecipeSummaryActivity,
                                RecipeDetailActivity::class.java
                            ).apply {
                                putExtra(RECIPE_ID, it.item.recipeId)
                                putExtra(RECIPE_STATE, it.item.state)
                            }
                            startActivity(intent)
                        }

                        is RecipeSummaryViewModel.ButtonClick.OpenRecipeEditor -> {
                            val intent = Intent(
                                this@RecipeSummaryActivity,
                                RecipeEditorActivity::class.java
                            ).apply {
                                putExtra(RECIPE_ID, it.item.recipeId)
                                putExtra(RECIPE_STATE, it.item.state)
                            }
                            startActivity(intent)
                        }
                    }
                }
        }
    }

    private fun setEventObservers() = with(recipeSummaryViewModel) {
        eventRecipeSummary.observe(this@RecipeSummaryActivity, EventObserver {
            when (it) {
                is RecipeSummaryViewModel.RecipeSummaryEvent.LoadError -> {
                    ConfirmDialogBuilder.create(
                        this@RecipeSummaryActivity,
                        getString(R.string.errorOccurs),
                        getString(R.string.ratatouilleErrorMassage)
                    ) {
                        finish()
                    }
                }

                RecipeSummaryViewModel.RecipeSummaryEvent.DeleteConfirm -> {
                    ConfirmDialogBuilder.create(
                        context = this@RecipeSummaryActivity,
                        title = getString(R.string.deleteConfirm),
                        content = getString(R.string.deleteConfirmContent),
                        showCancel = true,
                        onCancelListener = null,
                        onConfirmListener = { recipeSummaryViewModel.deleteRecipe() }
                    )
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.DeleteFinish -> {
                    if (it.flag) {
                        ConfirmDialogBuilder.create(
                            this@RecipeSummaryActivity,
                            getString(R.string.deleteFinish),
                            getString(R.string.deleteFinishContent)
                        ) {
                            finish()
                        }
                    } else {
                        showSnackBar(getString(R.string.deleteFailed))
                    }
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.UploadFinish -> {
                    val message = if (it.flag) getString(R.string.uploadSuccess) else getString(R.string.uploadFailed)
                    showSnackBar(message)
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.SaveFinish -> {
                    val message = if (it.flag) getString(R.string.saveSuccess) else getString(R.string.saveFailed)
                    showSnackBar(message)
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.UpdateFavoriteFinish -> {
                    val message = when (it.result) {
                        UpdateFavoriteResult.ADD -> getString(R.string.favoriteAddSuccess)
                        UpdateFavoriteResult.REMOVE -> getString(R.string.favoriteRemoveSuccess)
                        UpdateFavoriteResult.ERROR -> getString(R.string.favoriteChangeSuccess)
                    }
                    showSnackBar(message)
                }
            }
        })
    }

    private fun initViewModel() {
        intent.extras?.let { bundle ->
            val recipeId = bundle.getString(RECIPE_ID)
            val recipeState = bundle.getSerializable(RECIPE_STATE) as? RecipeState
            recipeSummaryViewModel.initViewModel(recipeId, recipeState)
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}
