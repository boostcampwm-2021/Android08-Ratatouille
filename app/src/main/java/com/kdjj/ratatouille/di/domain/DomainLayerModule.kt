package com.kdjj.ratatouille.di.domain

import com.kdjj.domain.di.ResultUseCaseModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [ResultUseCaseModule::class])
@InstallIn(SingletonComponent::class)
class DomainLayerModule
