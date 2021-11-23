package com.kdjj.presentation.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.kdjj.presentation.R

object Notifications {

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val timerChannel = NotificationChannel(
                TIMER_CHANNEL_ID,
                context.getString(R.string.ratatouilleTimer),
                NotificationManager.IMPORTANCE_LOW
            )
            NotificationManagerCompat.from(context).createNotificationChannel(timerChannel)

            val alarmChannel = NotificationChannel(
                ALARM_CHANNEL_ID,
                context.getString(R.string.ratatouilleAlarm),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            NotificationManagerCompat.from(context).createNotificationChannel(alarmChannel)
        }
    }

    private const val ALARM_CHANNEL_ID = "ID_RATATOUILLE_ALARM"
    private const val TIMER_CHANNEL_ID = "ID_RATATOUILLE_TIMER"
}