package com.kdjj.ratatouille.di.data

import com.kdjj.data.di.RepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RepositoryModule::class])
@InstallIn(SingletonComponent::class)
abstract class DataLayerModule
