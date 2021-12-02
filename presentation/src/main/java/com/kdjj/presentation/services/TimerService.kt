package com.kdjj.presentation.services


import android.content.Intent
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LifecycleService
import com.kdjj.presentation.common.ACTION_START
import com.kdjj.presentation.common.Notifications

class TimerService : LifecycleService() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { intent ->
            if (intent.action == ACTION_START) {
                startForeground(
                    FOREGROUND_NOTIFICATION_ID,
                    Notifications.createForegroundNotificationBuilder(this).build()
                )
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        const val FOREGROUND_NOTIFICATION_ID = 1
    }
}
