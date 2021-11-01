package com.kdjj.ratatouille.di

import android.content.Context
import com.kdjj.presentation.di.PresentationSubComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [SubComponentModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun presentationComponent(): PresentationSubComponent.Factory
}