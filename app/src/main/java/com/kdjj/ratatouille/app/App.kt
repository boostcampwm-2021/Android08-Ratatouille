package com.kdjj.ratatouille.app

import android.app.Application
import com.kdjj.presentation.di.PresentationComponentProvider
import com.kdjj.presentation.di.PresentationSubComponent
import com.kdjj.ratatouille.di.AppComponent
import com.kdjj.ratatouille.di.DaggerAppComponent

class App : Application(), PresentationComponentProvider {

    val appComponent: AppComponent by lazy {
        initializeComponent()
    }

    private fun initializeComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

    override fun providePresentationComponent(): PresentationSubComponent {
        return appComponent.presentationComponent().create()
    }
}