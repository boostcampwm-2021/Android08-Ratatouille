package com.kdjj.presentation.viewmodel.recipedetail

import android.media.Ringtone
import androidx.lifecycle.*
import com.kdjj.domain.model.Recipe
import com.kdjj.domain.model.RecipeState
import com.kdjj.domain.model.RecipeStep
import com.kdjj.domain.model.request.FetchOthersRecipeRequest
import com.kdjj.domain.model.request.GetMyRecipeRequest
import com.kdjj.domain.usecase.ResultUseCase
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.common.Notifications
import com.kdjj.presentation.model.StepTimerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val ringtone: Ringtone,
    private val getMyRecipeUseCase: ResultUseCase<GetMyRecipeRequest, Recipe>,
    private val fetchOthersRecipeUseCase: ResultUseCase<FetchOthersRecipeRequest, Recipe>,
    private val notifications: Notifications
) : ViewModel() {

    private val _liveStepList = MutableLiveData<List<RecipeStep>>()
    val liveStepList: LiveData<List<RecipeStep>> get() = _liveStepList
    val liveModelList = liveStepList.map { stepList ->
        stepList.map { step ->
            StepTimerModel(step, notifications) {
                ringtone.play()
                _liveTimerList.value?.indexOf(it)?.let { idx ->
                    _eventRecipeDetail.value = Event(RecipeDetailEvent.MoveToTimer(idx))
                }
                it.startAnimation()
            }
        }
    }

    private val _liveTimerList = MutableLiveData<List<StepTimerModel>>(listOf())
    val liveTimerList: LiveData<List<StepTimerModel>> get() = _liveTimerList

    private val _liveStepBarSelected = MutableLiveData<RecipeStep>()
    val liveStepBarSelected: LiveData<RecipeStep> get() = _liveStepBarSelected

    private val _liveLoading = MutableLiveData(false)
    val liveLoading: LiveData<Boolean> get() = _liveLoading

    private val _liveTitle = MutableLiveData<String>()
    val liveTitle: LiveData<String> get() = _liveTitle

    private var isInitialized = false

    private val _eventRecipeDetail = MutableLiveData<Event<RecipeDetailEvent>>()
    val eventRecipeDetail: LiveData<Event<RecipeDetailEvent>> get() = _eventRecipeDetail

    sealed class RecipeDetailEvent {
        class MoveToStepBar(val idx: Int) : RecipeDetailEvent()
        class MoveToStep(val idx: Int) : RecipeDetailEvent()
        class MoveToTimer(val idx: Int) : RecipeDetailEvent()
        object OpenTimer : RecipeDetailEvent()
        class CloseTimer(val onAnimationEnd: () -> Unit) : RecipeDetailEvent()
        object Error : RecipeDetailEvent()
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
                RecipeState.NETWORK ->
                    fetchOthersRecipeUseCase(FetchOthersRecipeRequest(recipeId))
                RecipeState.CREATE, RecipeState.DOWNLOAD, RecipeState.UPLOAD ->
                    getMyRecipeUseCase(GetMyRecipeRequest(recipeId))
            }.onSuccess { recipe ->
                _liveStepList.value = recipe.stepList
                _liveStepBarSelected.value = recipe.stepList.first()
                _liveTitle.value = recipe.title
            }.onFailure {
                _eventRecipeDetail.value = Event(RecipeDetailEvent.Error)
            }
            _liveLoading.value = false
        }

        isInitialized = true
    }

    fun scrollToStep(step: RecipeStep) {
        _liveStepList.value?.indexOf(step)?.let { idx ->
            _eventRecipeDetail.value = Event(RecipeDetailEvent.MoveToStep(idx))
        }
    }

    fun updateCurrentStepTo(idx: Int) {
        val step = _liveStepList.value?.getOrNull(idx) ?: return
        if (_liveStepBarSelected.value?.stepId != step.stepId) {
            _liveStepBarSelected.value = step
            _eventRecipeDetail.value = Event(RecipeDetailEvent.MoveToStepBar(idx))
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

    fun setBackground(state: Boolean){
        _liveTimerList.value?.forEach { stepTimerModel ->
            stepTimerModel.setBackground(state)
        }
    }

    override fun onCleared() {
        _liveTimerList.value?.forEach { model ->
            model.pause()
        }
        super.onCleared()
    }
}
