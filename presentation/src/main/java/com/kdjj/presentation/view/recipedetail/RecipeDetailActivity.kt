package com.kdjj.presentation.view.recipedetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.domain.model.*
import com.kdjj.presentation.R
import com.kdjj.presentation.databinding.ActivityRecipeDetailBinding
import com.kdjj.presentation.view.adapter.RecipeDetailStepListAdapter
import com.kdjj.presentation.view.adapter.RecipeDetailTimerListAdapter
import com.kdjj.presentation.view.adapter.RecipeEditorListAdapter
import com.kdjj.presentation.viewmodel.recipedetail.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel: RecipeDetailViewModel by viewModels()

    private lateinit var stepListAdapter: RecipeDetailStepListAdapter
    private lateinit var timerListAdapter: RecipeDetailTimerListAdapter

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
        ItemTouchHelper.UP
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPos = viewHolder.absoluteAdapterPosition
            val toPos = target.absoluteAdapterPosition
            viewModel.moveTimer(fromPos, toPos)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            viewModel.removeTimerAt(viewHolder.absoluteAdapterPosition)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_detail)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        stepListAdapter = RecipeDetailStepListAdapter(viewModel)
        binding.recyclerViewDetailStep.adapter = stepListAdapter

        timerListAdapter = RecipeDetailTimerListAdapter(viewModel)
        binding.recyclerViewDetailTimer.adapter = timerListAdapter
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.recyclerViewDetailTimer)

        val recipe = Recipe(
            "",
            "레시피 제목",
            RecipeType(1, "기타"),
            "",
            "",
            listOf(
                RecipeStep("", "단계1", RecipeStepType.PREPARE, "1단계입니다.", "", 0),
                RecipeStep("", "단계2", RecipeStepType.COOK, "2단계입니다. 약 20초 걸립니다.", "", 20),
                RecipeStep("", "단계3", RecipeStepType.COOK, "3단계입니다. 약 2분 30초 걸립니다.", "", 150),
                RecipeStep("", "단계4", RecipeStepType.COOK, "4단계입니다. 약 10초 걸립니다.", "", 10),
            ),
            "",
            0,
            false,
            0,
            RecipeState.CREATE
        )

        setSupportActionBar(binding.toolbarDetail)
        title = recipe.title

        viewModel.initializeWith(recipe)
    }
}