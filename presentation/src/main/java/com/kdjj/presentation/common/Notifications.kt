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
import com.kdjj.presentation.view.recipedetail.RecipeDetailActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Notifications @Inject constructor(
    @ApplicationContext private val context: Context
) {

     fun showTimer(stepId: String, stepName: String, timeLeft: Int) {
        val builder = NotificationCompat.Builder(context, TIMER_CHANNEL_ID)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(stepName)
            .setContentText(String.format("%02d:%02d", timeLeft / 60, timeLeft % 60))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(context))

        NotificationManagerCompat.from(context)
            .notify(stepId.hashCode(), builder.build())
    }

    fun showAlarm(stepId: String, stepName: String) {
        val builder = NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.timerEnd, stepName))
            .setContentText(context.getString(R.string.timerEndContent, stepName))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(getPendingIntent(context))

        NotificationManagerCompat.from(context)
            .notify(stepId.hashCode(), builder.build())
    }

    companion object {
        private const val ALARM_CHANNEL_ID = "ID_RATATOUILLE_ALARM"
        private const val TIMER_CHANNEL_ID = "ID_RATATOUILLE_TIMER"
        private const val FOREGROUND_ID = "ID_FOREGROUND"

        fun createForegroundNotificationBuilder(context: Context) =
            NotificationCompat.Builder(context, FOREGROUND_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.ratatouille))
                .setContentText(context.getString(R.string.appIsRunning))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(getPendingIntent(context))

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

                val foregroundChannel = NotificationChannel(
                    FOREGROUND_ID,
                    context.getString(R.string.ratatouilleForeground),
                    NotificationManager.IMPORTANCE_HIGH
                )
                NotificationManagerCompat.from(context).createNotificationChannel(foregroundChannel)
            }
        }

        private fun getPendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, RecipeDetailActivity::class.java)
            return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun cancelAllNotification(context: Context) {
            NotificationManagerCompat.from(context).cancelAll()
        }
    }
}