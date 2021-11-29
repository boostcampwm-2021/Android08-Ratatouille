package com.kdjj.presentation.model

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.common.Event
import com.kdjj.presentation.common.Notifications

class StepTimerModel (
    val recipeStep: RecipeStep,
    private val notifications: Notifications,
    private val onFinishListener: (StepTimerModel) -> Unit
) {
    private var leftMillis = recipeStep.seconds * 1000L
        set(value) {
            _liveLeftSeconds.value = (value / 1000).toInt()
            field = value
        }
    private val _liveLeftSeconds = MutableLiveData((leftMillis / 1000).toInt())
    val liveLeftSeconds: LiveData<Int> get() = _liveLeftSeconds

    private val _liveState = MutableLiveData(TimerState.INITIALIZED)
    val liveState: LiveData<TimerState> get() = _liveState

    private lateinit var timer: CountDownTimer

    private val _eventAnimation = MutableLiveData<Event<Unit>>()
    val eventAnimation: LiveData<Event<Unit>> get() = _eventAnimation

    private var isRunningOnBackground = false

    fun reset() {
        timer.cancel()
        leftMillis = recipeStep.seconds * 1000L
        _liveState.value = TimerState.INITIALIZED
    }

    fun pause() {
        timer.cancel()
        _liveState.value = TimerState.PAUSED
    }

    fun resume() {
        if (_liveState.value != TimerState.PAUSED && _liveState.value != TimerState.INITIALIZED)
            return

        timer = StepTimer(leftMillis, {
            leftMillis = it
            if(isRunningOnBackground){
                notifications.showTimer(
                    recipeStep.stepId,
                    recipeStep.name,
                    (leftMillis / 1000).toInt(),
                )
            }
        }, {
            onFinishListener(this)
            if(isRunningOnBackground){
                notifications.showAlarm(
                    recipeStep.stepId,
                    recipeStep.name
                )
            }
        }).start()
        _liveState.value = TimerState.RUNNING
    }

    fun startAnimation() {
        _liveState.value = TimerState.END
        _eventAnimation.value = Event(Unit)
    }

    fun setBackground(state: Boolean){
        isRunningOnBackground = state
    }

    enum class TimerState {
        INITIALIZED,
        RUNNING,
        PAUSED,
        END
    }
}
