package com.kdjj.presentation.viewmodel.recipedetail

import android.media.Ringtone
import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.request.FetchRemoteRecipeRequest
import com.kdjj.domain.model.request.GetLocalRecipeRequest
import com.kdjj.domain.usecase.FlowUseCase
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.model.StepTimerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val ringtone: Ringtone,
    private val getLocalRecipeFlowUseCase: FlowUseCase<GetLocalRecipeRequest, Recipe>,
    private val fetchRemoteRecipeUseCase: ResultUseCase<FetchRemoteRecipeRequest, Recipe>
) : ViewModel() {

    private val _liveStepList = MutableLiveData<List<RecipeStep>>()
    val liveStepList: LiveData<List<RecipeStep>> get() = _liveStepList
    val liveModelList = liveStepList.map { stepList ->
        stepList.map { step ->
            StepTimerModel(step) {
                ringtone.play()
                _liveFinishedTimerPosition.value = _liveTimerList.value?.indexOf(it)
                it.startAnimation()
            }
        }
    }

    private val _liveSelectedStep = MutableLiveData<RecipeStep>()
    val liveSelectedStep: LiveData<RecipeStep> get() = _liveSelectedStep

    private val _liveMoveToIdx = MutableLiveData<Int>(0)
    val liveMoveToIdx: LiveData<Int> get() = _liveMoveToIdx

    private val _liveTimerList = MutableLiveData<List<StepTimerModel>>(listOf())
    val liveTimerList: LiveData<List<StepTimerModel>> get() = _liveTimerList

    private var _liveFinishedTimerPosition = MutableLiveData<Int>()
    val liveFinishedTimerPosition: LiveData<Int> get() = _liveFinishedTimerPosition

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    private val _liveTitle = MutableLiveData<String>()
    val liveTitle: LiveData<String> get() = _liveTitle

    private var isInitialized = false

    private val _eventRecipeDetail = MutableLiveData<Event<RecipeDetailEvent>>()
    val eventRecipeDetail: LiveData<Event<RecipeDetailEvent>> get() = _eventRecipeDetail

    sealed class RecipeDetailEvent {
        object OpenTimer: RecipeDetailEvent()
        class CloseTimer(val onAnimationEnd: () -> Unit): RecipeDetailEvent()
        object Error: RecipeDetailEvent()
    }

    fun initializeWith(recipeId: String?, state: RecipeState?) {
        if (isInitialized) return

        if (recipeId == null || state == null) {
            _eventRecipeDetail.value = Event(RecipeDetailEvent.Error)
            return
        }

        _liveLoading.value = true
        viewModelScope.launch {
            when (state) {
                RecipeState.NETWORK -> {
                    fetchRemoteRecipeUseCase(FetchRemoteRecipeRequest(recipeId))
                            .onSuccess { recipe ->
                                _liveStepList.value = recipe.stepList
                                selectStep(recipe.stepList[0])
                                _liveTitle.value = recipe.title
                            }
                            .onFailure {
                                _eventRecipeDetail.value = Event(RecipeDetailEvent.Error)
                            }
                }
                RecipeState.CREATE,
                RecipeState.DOWNLOAD,
                RecipeState.UPLOAD -> {
                    val recipeFlow = getLocalRecipeFlowUseCase(GetLocalRecipeRequest(recipeId))
                    val recipe = recipeFlow.first()
                    _liveStepList.value = recipe.stepList
                    selectStep(recipe.stepList[0])
                    _liveTitle.value = recipe.title
                }
            }
            _liveLoading.value = false
        }

        isInitialized = true
    }

    fun selectStep(step: RecipeStep) {
        _liveSelectedStep.value = step
        _liveMoveToIdx.value = _liveStepList.value?.indexOf(step)
    }

    fun showScrolledTo(idx: Int) {
        val step = _liveStepList.value?.getOrNull(idx) ?: return
        if (_liveSelectedStep.value?.stepId != step.stepId) {
            _liveSelectedStep.value = step
        }
    }

    fun toggleTimer(model: StepTimerModel) {
        when (model.liveState.value) {
            StepTimerModel.TimerState.INITIALIZED -> {
                _liveTimerList.value?.let { timerList ->
                    if (timerList.isEmpty()) {
                        _eventRecipeDetail.value = Event(RecipeDetailEvent.OpenTimer)
                    }
                    _liveTimerList.value = timerList + model
                    model.resume()
                }
            }
            StepTimerModel.TimerState.RUNNING -> {
                model.pause()
            }
            StepTimerModel.TimerState.PAUSED -> {
                model.resume()
            }
            StepTimerModel.TimerState.END -> {
                removeTimer(model)
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
            removeTimer(model)
        }
    }

    private fun removeTimer(timerModel: StepTimerModel) {
        _liveTimerList.value?.let { modelList ->
            timerModel.pause()
            if (modelList.size == 1) {
                _eventRecipeDetail.value =
                    Event(RecipeDetailEvent.CloseTimer {
                        _liveTimerList.value = modelList.toMutableList().apply {
                            remove(timerModel)
                            timerModel.reset()
                        }
                    })
            } else {
                _liveTimerList.value = modelList.toMutableList().apply {
                    remove(timerModel)
                    timerModel.reset()
                }
            }
        }
    }

    override fun onCleared() {
        _liveTimerList.value?.forEach { model ->
            model.pause()
        }
        super.onCleared()
    }
}
