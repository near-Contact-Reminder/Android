package com.alarmy.near.network.di

import com.alarmy.near.network.service.AuthService
import com.alarmy.near.network.service.FriendService
import com.alarmy.near.network.service.MemberApiService
import com.alarmy.near.network.service.MessagingService
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
    fun provideAuthService(retrofit: Retrofit): AuthService = retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideFriendService(retrofit: Retrofit): FriendService = retrofit.create(FriendService::class.java)

    @Provides
    @Singleton
    fun provideMemberApiService(retrofit: Retrofit): MemberApiService = retrofit.create(MemberApiService::class.java)

    @Provides
    @Singleton
    fun provideMessageService(retrofit: Retrofit): MessagingService = retrofit.create(MessagingService::class.java)
}
