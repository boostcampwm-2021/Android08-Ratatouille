package com.kdjj.presentation.viewmodel.recipedetail

import android.media.Ringtone
import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.StepTimerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    // TODO("fetchLocalRecipeById")
    private val ringtone: Ringtone
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

    private val _eventOpenTimer = MutableLiveData<Event<Unit>>()
    val eventOpenTimer: LiveData<Event<Unit>> get() = _eventOpenTimer

    private val _eventCloseTimer = MutableLiveData<Event<() -> Unit>>()
    val eventCloseTimer: LiveData<Event<() -> Unit>> get() = _eventCloseTimer

    private var _liveFinishedTimerPosition = MutableLiveData<Int>()
    val liveFinishedTimerPosition: LiveData<Int> get () = _liveFinishedTimerPosition

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
                if (timerList.isEmpty()) {
                    _eventOpenTimer.value = Event(Unit)
                }
                _liveTimerList.value = timerList + StepTimerModel(step) {
                    ringtone.play()
                    _liveFinishedTimerPosition.value = _liveTimerList.value?.indexOf(it)
                    it.startAnimation()
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
            timerModel.pause()
            if (modelList.size == 1) {
                _eventCloseTimer.value = Event {
                    _liveTimerList.value = modelList.toMutableList().apply {
                        remove(timerModel)
                    }
                }
            } else {
                _liveTimerList.value = modelList.toMutableList().apply {
                    remove(timerModel)
                }
            }
        }
    }
}