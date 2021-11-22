package com.kdjj.presentation.viewmodel.recipedetail

import android.media.Ringtone
import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.request.FetchRemoteRecipeRequest
import com.kdjj.domain.model.request.GetLocalRecipeFlowRequest
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
    private val getLocalRecipeFlowUseCase: FlowUseCase<GetLocalRecipeFlowRequest, Recipe>,
    private val fetchRemoteRecipeUseCase: ResultUseCase<FetchRemoteRecipeRequest, Recipe>
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
                    val recipeFlow = getLocalRecipeFlowUseCase(GetLocalRecipeFlowRequest(recipeId))
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
    }

    fun addTimer(step: RecipeStep) {
        _liveTimerList.value?.let { timerList ->
            if (!timerList.any { it.recipeStep == step }) {
                if (timerList.isEmpty()) {
                    _eventRecipeDetail.value = Event(RecipeDetailEvent.OpenTimer)

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
                _eventRecipeDetail.value =
                    Event(RecipeDetailEvent.CloseTimer {
                        _liveTimerList.value = modelList.toMutableList().apply {
                            remove(timerModel)
                        }
                    })
            } else {
                _liveTimerList.value = modelList.toMutableList().apply {
                    remove(timerModel)
                }
            }
        }
    }
}
