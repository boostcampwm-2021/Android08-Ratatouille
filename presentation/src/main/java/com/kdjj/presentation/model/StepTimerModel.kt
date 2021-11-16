package com.kdjj.presentation.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kdjj.domain.model.RecipeStep

class StepTimerModel(
    val recipeStep: RecipeStep,
    private val onFinishListener: (StepTimerModel) -> Unit
) {
    private val _liveLeftSeconds = MutableLiveData(recipeStep.seconds)
    val liveLeftSeconds: LiveData<Int> get() = _liveLeftSeconds

    private val timer = StepTimer(recipeStep.seconds, {
        _liveLeftSeconds.value = (it / 1000).toInt()
    }, {
        onFinishListener(this)
    })

    init {
        timer.start()
    }
}
