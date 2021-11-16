package com.kdjj.presentation.model

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kdjj.domain.model.RecipeStep

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

    private val _liveRunning = MutableLiveData(true)
    val liveRunning: LiveData<Boolean> get() = _liveRunning

    private lateinit var timer: CountDownTimer

    init {
        resume()
    }

    fun pause() {
        timer.cancel()
        _liveRunning.value = false
    }

    fun resume() {
        timer = StepTimer(leftMillis, {
            leftMillis = it
        }, {
            onFinishListener(this)
        }).start()
        _liveRunning.value = true
    }
}
