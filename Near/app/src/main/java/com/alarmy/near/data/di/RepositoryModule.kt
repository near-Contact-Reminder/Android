package com.alarmy.near.data.di

import com.alarmy.near.data.repository.AuthRepository
import com.alarmy.near.data.repository.AuthRepositoryImpl
import com.alarmy.near.data.repository.DefaultFriendRepository
import com.alarmy.near.data.repository.ExampleRepository
import com.alarmy.near.data.repository.ExampleRepositoryImpl
import com.alarmy.near.data.repository.FriendRepository
import com.alarmy.near.data.repository.MemberRepository
import com.alarmy.near.data.repository.MemberRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindExampleRepository(exampleRepositoryImpl: ExampleRepositoryImpl): ExampleRepository

    @Binds
    @Singleton
    abstract fun bindFriendRepository(friendRepository: DefaultFriendRepository): FriendRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindMemberRepository(memberRepositoryImpl: MemberRepositoryImpl): MemberRepository
}
