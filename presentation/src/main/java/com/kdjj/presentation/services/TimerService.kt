package com.kdjj.presentation.services


import android.content.Intent
import android.os.CountDownTimer
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.kdjj.presentation.R
import com.kdjj.presentation.common.Notifications

class TimerService : LifecycleService() {

    class ServiceTimer(
        time: Long,
        private val onTickListener: (Long) -> Unit
    ) : CountDownTimer(time, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            onTickListener(millisUntilFinished)
        }
        override fun onFinish() {}
    }

    data class StepTimerItem(val time: Int, val stepId: String, val stepName: String)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { intent ->
            when (intent.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    println("service start")
                    val timerStrList = intent.getStringArrayExtra("TIMERS") ?: arrayOf()

                    val timerList = timerStrList.map { timerStr ->
                        val (timeStr, stepId, stepName) = timerStr.split(":", limit=3)
                        StepTimerItem(timeStr.toInt(), stepId, stepName)
                    }.sortedByDescending { it.time }

                    val maxMillis = timerList.maxOf { it.time } * 1000L
                    ServiceTimer(maxMillis) { millisLeft ->
                        println("millisLeft ${millisLeft}")
                        val timeElapsed = maxMillis - millisLeft
                        timerList.forEach { item ->
                            val timeLeft = (item.time - timeElapsed / 1000).toInt()
                            println("${item} : ${timeLeft}")
                            startForeGroundService(item, timeLeft)
                        }
                    }.start()
                }
                else -> {
                }
            }

        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForeGroundService(item: StepTimerItem, timeLeft: Int) {
        val builder = NotificationCompat.Builder(this, Notifications.TIMER_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(item.stepName)
            .setContentText("${timeLeft}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        startForeground(item.stepId.hashCode(), builder.build())
    }

    companion object {
        const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
    }
}