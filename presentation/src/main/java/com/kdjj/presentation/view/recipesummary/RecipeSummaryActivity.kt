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
import com.google.android.material.snackbar.Snackbar
import com.kdjj.domain.model.RecipeState
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.databinding.ActivityRecipeSummaryBinding
import com.kdjj.presentation.model.RecipeSummaryType
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.view.dialog.CustomProgressDialog
import com.kdjj.presentation.view.recipedetail.RecipeDetailActivity
import com.kdjj.presentation.view.recipeeditor.RecipeEditorActivity
import com.kdjj.presentation.viewmodel.recipesummary.RecipeSummaryViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

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

    private val compositeDisposable = CompositeDisposable()

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
        summarySubject.throttleFirst(1, TimeUnit.SECONDS)
            .subscribe {
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
            }.also {
                compositeDisposable.add(it)
            }
    }

    private fun setEventObservers() = with(recipeSummaryViewModel) {
        eventRecipeSummary.observe(this@RecipeSummaryActivity, EventObserver {
            when (it) {
                is RecipeSummaryViewModel.RecipeSummaryEvent.LoadError -> {
                    ConfirmDialogBuilder.create(
                        this@RecipeSummaryActivity,
                        "오류 발생",
                        "레시피를 들고오던 라따뚜이가 넘어졌습니다..ㅠㅠ\n확인버튼을 누르면 이전 화면으로 돌아갑니다."
                    ) {
                        finish()
                    }
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.DeleteFinish -> {
                    if (it.flag) {
                        ConfirmDialogBuilder.create(
                            this@RecipeSummaryActivity,
                            "삭제 완료",
                            "레시피가 정상적으로 삭제되었습니다.\n확인을 눌러 이전화면으로 돌아가주세요."
                        ) {
                            finish()
                        }
                    } else {
                        showSnackBar("삭제 실패")
                    }
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.UploadFinish -> {
                    val message = if (it.flag) "업로드 성공" else "업로드 실패"
                    showSnackBar(message)
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.SaveFinish -> {
                    val message = if (it.flag) "저장 성공" else "저장 실패"
                    showSnackBar(message)
                }

                is RecipeSummaryViewModel.RecipeSummaryEvent.UpdateFavoriteFinish -> {
                    val message = if (it.flag) "즐겨찾기 추가 / 제거 성공" else "즐겨찾기 추가 / 제거 실패"
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

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
