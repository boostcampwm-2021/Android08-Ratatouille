package com.kdjj.presentation.common

import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IdGenerator @Inject constructor(@ApplicationContext private val context: Context) {
    private var lastGenerateTime: Long = 0L

    fun generateId(): String {
        var currentTime = System.currentTimeMillis()
        while (currentTime == lastGenerateTime) {
            currentTime++
        }
        lastGenerateTime = currentTime
        return getDeviceId() + currentTime
    }

    fun getDeviceId(): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}