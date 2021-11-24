package com.kdjj.presentation.view.recipedetail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kdjj.domain.model.*
import com.kdjj.presentation.R
import com.kdjj.presentation.common.*
import com.kdjj.presentation.databinding.ActivityRecipeDetailBinding
import com.kdjj.presentation.services.TimerService
import com.kdjj.presentation.view.adapter.RecipeDetailStepListAdapter
import com.kdjj.presentation.view.adapter.RecipeDetailTimerListAdapter
import com.kdjj.presentation.view.adapter.RecipeEditorListAdapter
import com.kdjj.presentation.view.dialog.ConfirmDialogBuilder
import com.kdjj.presentation.view.dialog.CustomProgressDialog
import com.kdjj.presentation.viewmodel.recipedetail.RecipeDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel: RecipeDetailViewModel by viewModels()

    private lateinit var loadingDialog: CustomProgressDialog

    private lateinit var stepListAdapter: RecipeDetailStepListAdapter
    private lateinit var timerListAdapter: RecipeDetailTimerListAdapter

    @Inject
    lateinit var displayConverter: DisplayConverter

    private val service by lazy { Intent(applicationContext, TimerService::class.java) }

    private var isExiting = false

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

        loadingDialog = CustomProgressDialog(this)

        stepListAdapter = RecipeDetailStepListAdapter(viewModel)
        binding.recyclerViewDetailStep.adapter = stepListAdapter

        timerListAdapter = RecipeDetailTimerListAdapter(viewModel)
        binding.recyclerViewDetailTimer.adapter = timerListAdapter
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(binding.recyclerViewDetailTimer)

        setSupportActionBar(binding.toolbarDetail)

        setObservers()
        loadRecipe()
    }

    private fun loadRecipe() {
        val (recipeId, recipeState) = intent.extras?.let { bundle ->
            bundle.getString(RECIPE_ID) to (bundle.getSerializable(RECIPE_STATE) as? RecipeState)
        } ?: null to null
        viewModel.initializeWith(recipeId, recipeState)
    }

    private fun setObservers() {

        viewModel.liveLoading.observe(this) { doLoading ->
            if (doLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }

        viewModel.liveTitle.observe(this) {
            title = it
        }

        viewModel.eventRecipeDetail.observe(this, EventObserver {
            when (it) {
                is RecipeDetailViewModel.RecipeDetailEvent.OpenTimer -> {
                    AnimatorSet().apply {
                        playTogether(
                            ObjectAnimator.ofFloat(
                                binding.recyclerViewDetailTimer,
                                View.TRANSLATION_Y,
                                displayConverter.dpToPx(-50),
                                0f
                            ),
                            ObjectAnimator.ofFloat(
                                binding.recyclerViewDetailTimer, View.ALPHA, 0f, 1f
                            )
                        )
                        duration = 500
                        start()
                    }
                }

                is RecipeDetailViewModel.RecipeDetailEvent.CloseTimer -> {
                    AnimatorSet().apply {
                        playTogether(
                            ObjectAnimator.ofFloat(
                                binding.recyclerViewDetailTimer,
                                View.TRANSLATION_Y,
                                0f,
                                displayConverter.dpToPx(-50)
                            ),
                            ObjectAnimator.ofFloat(
                                binding.recyclerViewDetailTimer, View.ALPHA, 1f, 0f
                            )
                        )
                        duration = 500
                        doOnEnd { _ ->
                            it.onAnimationEnd()
                        }
                        start()
                    }
                }

                is RecipeDetailViewModel.RecipeDetailEvent.Error -> {
                    ConfirmDialogBuilder.create(
                        this,
                        "오류 발생",
                        "레시피를 들고오던 라따뚜이가 넘어졌습니다..ㅠㅠ\n확인버튼을 누르면 이전 화면으로 돌아갑니다."
                    ) {
                        finish()
                    }
                }
            }
        })
    }

    override fun onRestart() {
        super.onRestart()
        applicationContext.stopService(service)
    }

    override fun onBackPressed() {
        isExiting = true
        super.onBackPressed()
    }

    override fun onStop() {
        if (!isExiting) {
            service.also {
                it.action = "ACTION_START"
                val timerList = viewModel.liveTimerList.value ?: return
                it.putExtra("TIMERS",
                    timerList.map { timer ->
                        "${timer.liveLeftSeconds.value ?: 0}:${timer.recipeStep.stepId}:${timer.recipeStep.name}"
                    }.toTypedArray()
                )
                applicationContext.startService(it)
            }
        }
        super.onStop()
    }
}