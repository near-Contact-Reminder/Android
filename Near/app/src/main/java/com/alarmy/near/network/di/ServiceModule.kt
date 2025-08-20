package com.alarmy.near.network.di

import com.alarmy.near.network.service.FriendService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {
    @Provides
    @Singleton
    fun provideFriendService(retrofit: Retrofit): FriendService = retrofit.create(FriendService::class.java)
}
