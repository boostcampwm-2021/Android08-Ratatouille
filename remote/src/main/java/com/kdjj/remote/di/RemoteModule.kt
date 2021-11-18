package com.kdjj.remote.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFireStore(): FirebaseFirestore {
        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        val fireStore = Firebase.firestore
        fireStore.firestoreSettings = settings
        return fireStore
    }

    @Provides
    @Singleton
    fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference
}
