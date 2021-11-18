package com.kdjj.presentation.view.recipesummary

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.kdjj.domain.model.RecipeState
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.databinding.ActivityRecipeSummaryBinding
import com.kdjj.presentation.model.RecipeSummaryType
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.viewmodel.recipesummary.RecipeSummaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeSummaryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRecipeSummaryBinding
    private val recipeSummaryViewModel: RecipeSummaryViewModel by viewModels()
    private val floatingMenuIdListMap: Map<RecipeSummaryType, List<Int>> = mapOf(
        RecipeSummaryType.MY_SAVE_RECIPE to listOf(
            R.id.uploadButton_summary,
            R.id.deleteButton_summary,
            R.id.editButton_summary,
            R.id.favoriteButton_summary,
        ),
        RecipeSummaryType.OTHER_SERVER_RECIPE to listOf(
            R.id.stealButton_summary,
            R.id.stealFavoriteButton_summary,
        ),
        RecipeSummaryType.MY_SERVER_RECIPE to listOf(
            R.id.deleteButton_summary
        ),
        RecipeSummaryType.MY_SAVE_OTHER_RECIPE to listOf(
            R.id.deleteButton_summary,
            R.id.editButton_summary,
            R.id.favoriteButton_summary,
        )
    )
    private val allFloatingButtonList = floatingMenuIdListMap.values
        .reduce { acc, list ->
            acc + list
        }.distinct()
    private var isFloatingActionButtonOpen = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_summary)
        binding.viewModel = recipeSummaryViewModel
        binding.lifecycleOwner = this
        
        setSupportActionBar(binding.toolbarSummary)
        
        initViewModel()
        initObserver()
    }
    
    private fun initObserver() = with(recipeSummaryViewModel) {
        eventNoInfo.observe(this@RecipeSummaryActivity, EventObserver {
            showNoInfoDialog()
        })
        liveRecipe.observe(this@RecipeSummaryActivity) { recipe ->
            title = recipe.title
        }
        
        eventInitView.observe(this@RecipeSummaryActivity, EventObserver { recipeSummaryType ->
            val menuButtonList = floatingMenuIdListMap[recipeSummaryType]?.map {
                findViewById<AppCompatButton>(it)
            }
            initFloatingMenuVisibility(menuButtonList)
        })
    }
    
    private fun initFloatingMenuVisibility(buttonList: List<AppCompatButton>?) = with(binding) {
        allFloatingButtonList.map { buttonId ->
            findViewById<AppCompatButton>(buttonId)
        }.forEach { button ->
            button.visibility = View.GONE
        }
        
        buttonList?.forEach { button ->
            button.visibility = View.VISIBLE
        }
    }
    
    private fun initViewModel() {
        intent.extras?.let { bundle ->
            val recipeId = bundle.getString(RECIPE_ID)
            val recipeState = bundle.getSerializable(RECIPE_STATE) as? RecipeState
            recipeSummaryViewModel.initViewModel(recipeId, recipeState)
        }
    }
    
    private fun showNoInfoDialog() {
        ConfirmDialogBuilder.create(
            this,
            "오류 발생",
            "레시피를 들고오던 라따뚜이가 넘어졌습니다..ㅠㅠ\n확인버튼을 누르면 이전 화면으로 돌아갑니다."
        ) {
            finish()
        }
    }
}
