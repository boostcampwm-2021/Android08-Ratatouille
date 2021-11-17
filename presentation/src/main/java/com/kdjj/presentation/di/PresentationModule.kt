package com.kdjj.presentation.di

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class PresentationModule {

    @Provides
    fun provideRingTone(@ApplicationContext context: Context): Ringtone {
        val notification1: Uri =
        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return RingtoneManager.getRingtone(
            context,
            notification1
        )
    }
}