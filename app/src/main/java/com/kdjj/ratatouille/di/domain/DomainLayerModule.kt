package com.kdjj.ratatouille.di.domain

import com.kdjj.domain.di.UseCaseModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [UseCaseModule::class])
@InstallIn(SingletonComponent::class)
class DomainLayerModule