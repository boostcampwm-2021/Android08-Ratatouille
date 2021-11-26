package com.kdjj.presentation.di

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import androidx.work.WorkManager
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
        return RingtoneManager.getRingtone(
            context,
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        )
    }

    @Provides
    fun provideWorkManager(
        @ApplicationContext context: Context
    ) = WorkManager.getInstance(context)
}
