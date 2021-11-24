package com.kdjj.presentation.model

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.common.Event

class StepTimerModel(
    val recipeStep: RecipeStep,
    private val onFinishListener: (StepTimerModel) -> Unit
) {
    private var leftMillis = recipeStep.seconds * 1000L
        set(value) {
            _liveLeftSeconds.value = (value / 1000).toInt()
            field = value
        }
    private val _liveLeftSeconds = MutableLiveData((leftMillis / 1000).toInt())
    val liveLeftSeconds: LiveData<Int> get() = _liveLeftSeconds

    private val _liveState = MutableLiveData(TimerState.PAUSED)
    val liveState: LiveData<TimerState> get() = _liveState

    private lateinit var timer: CountDownTimer

    private val _eventAnimation = MutableLiveData<Event<Unit>>()
    val eventAnimation: LiveData<Event<Unit>> get() = _eventAnimation

    init {
        resume()
    }

    fun pause() {
        if (_liveState.value != TimerState.RUNNING) return
        timer.cancel()
        _liveState.value = TimerState.PAUSED
    }

    fun resume() {
        if (_liveState.value != TimerState.PAUSED) return
        timer = StepTimer(leftMillis, {
            leftMillis = it
        }, {
            onFinishListener(this)
        }).start()
        _liveState.value = TimerState.RUNNING
    }

    fun startAnimation() {
        _liveState.value = TimerState.END
        _eventAnimation.value = Event(Unit)
    }

    enum class TimerState {
        RUNNING,
        PAUSED,
        END
    }
}
