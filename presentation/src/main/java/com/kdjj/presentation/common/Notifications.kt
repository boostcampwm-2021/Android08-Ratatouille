package com.kdjj.presentation.common

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.kdjj.presentation.R
import com.kdjj.presentation.viewmodel.recipedetail.NotificationBroadcastReceiver

object Notifications {

    fun showAlarm(context: Context, stepId: String, stepName: String) {
        val builder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.timerEnd, stepName))
            .setContentText(context.getString(R.string.timerEndContent, stepName))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(context))

        NotificationManagerCompat.from(context)
            .notify(stepId.hashCode(), builder.build())
    }

    fun showTimer(context: Context, stepId: String, stepName: String, timeLeft: Int) {
        val builder = NotificationCompat.Builder(context, TIMER_CHANNEL_ID)
            .setAutoCancel(false)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(stepName)
            .setContentText(String.format("%02d:%02d", timeLeft/60 , timeLeft%60))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(context))

        NotificationManagerCompat.from(context)
            .notify(stepId.hashCode(), builder.build())
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

    fun cancelAllNotification(context: Context){
        NotificationManagerCompat.from(context).cancelAll()
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private const val ALARM_CHANNEL_ID = "ID_RATATOUILLE_ALARM"
    const val TIMER_CHANNEL_ID = "ID_RATATOUILLE_TIMER"
}