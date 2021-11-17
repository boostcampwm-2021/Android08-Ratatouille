package com.kdjj.presentation.view.recipesummary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kdjj.domain.model.RecipeState
import com.kdjj.presentation.R
import com.kdjj.presentation.common.EventObserver
import com.kdjj.presentation.common.RECIPE_ID
import com.kdjj.presentation.common.RECIPE_STATE
import com.kdjj.presentation.databinding.ActivityRecipeSummaryBinding
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.viewmodel.recipesummary.RecipeSummaryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeSummaryActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRecipeSummaryBinding
    private val recipeSummaryViewModel: RecipeSummaryViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_summary)
        binding.viewModel = recipeSummaryViewModel
        binding.lifecycleOwner = this
        
        setSupportActionBar(binding.toolbarSummary)
        
        initViewModel()
        initObserver()
    }
    
    private fun initObserver() = with(recipeSummaryViewModel){
        eventNoInfo.observe(this@RecipeSummaryActivity, EventObserver{
            showNoInfoDialog()
        })
        liveRecipe.observe(this@RecipeSummaryActivity){ recipe ->
            title = recipe.title
        }
    }
    
    private fun initViewModel() {
        intent.extras?.let{ bundle ->
            val recipeId = bundle.getString(RECIPE_ID)
            val recipeState = bundle.getSerializable(RECIPE_STATE) as? RecipeState
            recipeSummaryViewModel.initViewModel(recipeId, recipeState)
        }
    }
    
    private fun showNoInfoDialog(){
        ConfirmDialogBuilder.create(
            this,
            "정보 에러",
            "전달되지 않은 정보가 존재합니다.\n확인을 눌러 이전화면으로 돌아갑니다."
        ){
            finish()
        }
    }
}
