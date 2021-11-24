package com.kdjj.presentation.services


import android.content.Intent
import android.os.CountDownTimer
import androidx.lifecycle.LifecycleService
import com.kdjj.presentation.common.Notifications

class TimerService : LifecycleService() {

    class ServiceTimer(
        time: Long,
        private val onTickListener: (Long) -> Unit,
        private val onFinishListener: () -> Unit
    ) : CountDownTimer(time, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            onTickListener(millisUntilFinished)
        }

        override fun onFinish() {
            onFinishListener()
        }
    }

    private var serviceTimer: ServiceTimer? = null

    data class StepTimerItem(val time: Int, val stepId: String, val stepName: String)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { intent ->
            if (intent.action == "ACTION_START") {
                val timerStrList = intent.getStringArrayExtra("TIMERS") ?: arrayOf()

                val timerList = timerStrList.map { timerStr ->
                    val (timeStr, stepId, stepName) = timerStr.split(":", limit = 3)
                    StepTimerItem(timeStr.toInt(), stepId, stepName)
                }.sortedByDescending { it.time }
                    .filter { it.time > 0 }

                if (timerList.isEmpty()) {
                    return@let
                }

                val checkAlarms = hashSetOf<String>()
                val maxMillis = (timerList.maxOf { it.time }) * 1000L
                serviceTimer = ServiceTimer(maxMillis, { millisLeft ->
                    val timeElapsed = maxMillis - millisLeft
                    timerList.forEach { item ->
                        val timeLeft = (item.time - timeElapsed / 1000).toInt()
                        if (timeLeft >= 0) {
                            Notifications.showTimer(
                                applicationContext,
                                item.stepId,
                                item.stepName,
                                timeLeft
                            )
                        } else if (!checkAlarms.contains(item.stepId)) {
                            Notifications.showAlarm(
                                applicationContext,
                                item.stepId,
                                item.stepName
                            )
                            checkAlarms.add(item.stepId)
                        }
                    }
                }) {
                    timerList.forEach { item ->
                        if (!checkAlarms.contains(item.stepId)) {
                            Notifications.showAlarm(
                                applicationContext,
                                item.stepId,
                                item.stepName
                            )
                            checkAlarms.add(item.stepId)
                        }
                    }
                }.apply { start() }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Notifications.cancelAllNotification(this)
        serviceTimer?.cancel()
        super.onDestroy()
    }
}
