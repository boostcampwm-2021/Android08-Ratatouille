package com.kdjj.presentation.model

import android.os.CountDownTimer

class StepTimer(
    leftTimeInMillis: Long,
    private val onTickListener: (Long) -> Unit,
    private val onFinishListener: () -> Unit
) : CountDownTimer(leftTimeInMillis, 100L) {

    override fun onTick(millisUntilFinished: Long) {
        onTickListener(millisUntilFinished)
    }

    override fun onFinish() {
        onFinishListener()
    }
}