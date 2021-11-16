package com.kdjj.presentation.model

import android.os.CountDownTimer

class StepTimer(
    seconds: Int,
    private val onTickListener: (Long) -> Unit,
    private val onFinishListener: () -> Unit
) : CountDownTimer(seconds * 1000L, 1000L) {

    override fun onTick(millisUntilFinished: Long) {
        onTickListener(millisUntilFinished)
    }

    override fun onFinish() {
        onFinishListener()
    }
}