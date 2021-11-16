package com.kdjj.presentation.viewmodel.recipedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.model.StepTimerModel
import javax.inject.Inject


class RecipeDetailViewModel @Inject constructor(
    // TODO("fetchLocalRecipeById")
) : ViewModel() {

    private val _liveStepList = MutableLiveData<List<RecipeStep>>()
    val liveStepList: LiveData<List<RecipeStep>> get() = _liveStepList

    private val _liveSelectedStep = MutableLiveData<RecipeStep>()
    val liveSelectedStep: LiveData<RecipeStep> get() = _liveSelectedStep

    private val _liveTimerList = MutableLiveData<List<StepTimerModel>>(listOf())
    val liveTimerList: LiveData<List<StepTimerModel>> get() = _liveTimerList

    fun initializeWith(recipe: Recipe) {
        _liveStepList.value = recipe.stepList
        selectStep(recipe.stepList[0])
    }

    fun selectStep(step: RecipeStep) {
        _liveSelectedStep.value = step
    }

    fun addTimer(step: RecipeStep) {
        _liveTimerList.value?.let { timerList ->
            if (!timerList.any { it.recipeStep == step }) {
                _liveTimerList.value = timerList + StepTimerModel(step) {
                    removeTimer(it)
                }
            }
        }
    }

    fun removeTimer(timerModel: StepTimerModel) {
        _liveTimerList.value?.let { modelList ->
            _liveTimerList.value = modelList.toMutableList().apply {
                remove(timerModel)
            }
        }
    }
}