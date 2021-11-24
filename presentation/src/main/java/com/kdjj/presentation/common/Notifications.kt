package com.kdjj.presentation.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kdjj.domain.model.RecipeStep
import com.kdjj.presentation.R
import com.kdjj.presentation.model.StepTimerModel

object Notifications {

    fun showAlarm(context: Context, step: RecipeStep) {
        val builder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.timerEnd, step.name))
            .setContentText(context.getString(R.string.timerEndContent, step.name))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context)
            .notify(step.stepId.hashCode(), builder.build())
    }

    fun showTimer(context: Context, step: RecipeStep, leftTime: Int) {
        val builder = NotificationCompat.Builder(context, TIMER_CHANNEL_ID)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("타이머")
            .setContentText("${leftTime}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        NotificationManagerCompat.from(context)
            .notify(step.stepId.hashCode(), builder.build())
    }

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
                NotificationManager.IMPORTANCE_HIGH
            )
            NotificationManagerCompat.from(context).createNotificationChannel(alarmChannel)
        }
    }

    private const val ALARM_CHANNEL_ID = "ID_RATATOUILLE_ALARM"
    const val TIMER_CHANNEL_ID = "ID_RATATOUILLE_TIMER"
}