package com.kdjj.presentation.viewmodel.recipedetail

import androidx.lifecycle.*
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

    val liveSelectedTimer: LiveData<StepTimerModel?> = MediatorLiveData<StepTimerModel?>().apply {
        addSource(_liveTimerList) { timerList ->
            value = timerList.firstOrNull { it.recipeStep == liveSelectedStep.value }
        }

        addSource(_liveSelectedStep) { step ->
            value = _liveTimerList.value?.firstOrNull { it.recipeStep == step }
        }
    }

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

    fun moveTimer(from: Int, to: Int) {
        _liveTimerList.value?.let { timerList ->
            _liveTimerList.value = timerList.toMutableList().apply {
                set(from, set(to, get(from)))
            }
        }
    }

    fun removeTimerAt(position: Int) {
        _liveTimerList.value?.getOrNull(position)?.let { model ->
            model.pause()
            removeTimer(model)
        }
    }

    private fun removeTimer(timerModel: StepTimerModel) {
        _liveTimerList.value?.let { modelList ->
            _liveTimerList.value = modelList.toMutableList().apply {
                timerModel.pause()
                remove(timerModel)
            }
        }
    }
}