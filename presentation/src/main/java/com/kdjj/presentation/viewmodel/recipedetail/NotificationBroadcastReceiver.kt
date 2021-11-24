package com.kdjj.presentation.viewmodel.recipedetail

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks
        if (tasks.isNotEmpty()) {
            tasks.forEach { task ->
                if (task.taskInfo.topActivity?.packageName == context.applicationContext.packageName) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        activityManager.moveTaskToFront(task.taskInfo.taskId, 0)
                    } else {
                        activityManager.moveTaskToFront(task.taskInfo.id, 0)
                    }
                }
            }
        }
    }
}