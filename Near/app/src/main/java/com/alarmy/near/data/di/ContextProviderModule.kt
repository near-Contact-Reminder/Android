package com.alarmy.near.data.di

import com.alarmy.near.data.provider.ActivityContextProvider
import com.alarmy.near.presentation.provider.ActivityContextProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Context Provider 모듈
 * Activity Context 제공을 위한 의존성 주입 설정
 */
@Module
@InstallIn(SingletonComponent::class)
interface ContextProviderModule {
    @Binds
    @Singleton
    fun bindActivityContextProvider(impl: ActivityContextProviderImpl): ActivityContextProvider
}

