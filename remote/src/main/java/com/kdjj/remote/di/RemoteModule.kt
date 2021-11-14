package com.kdjj.remote.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {
    
    @Provides
    @Singleton
    fun provideFireStore() = Firebase.firestore
    
    @Provides
    @Singleton
    fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference
}
