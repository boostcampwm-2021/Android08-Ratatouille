package com.kdjj.presentation.di

import com.kdjj.presentation.view.splash.LaunchActivity
import dagger.Subcomponent

@Subcomponent
interface PresentationSubComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): PresentationSubComponent
    }

    fun inject(activity: LaunchActivity)
}