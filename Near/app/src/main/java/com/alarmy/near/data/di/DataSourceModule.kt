package com.alarmy.near.data.di

import com.alarmy.near.data.datasource.KakaoDataSource
import com.alarmy.near.data.datasource.SocialLoginDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
interface DataSourceModule {
    @Binds
    @IntoSet
    abstract fun bindKakaoDataSource(kakaoDataSource: KakaoDataSource): SocialLoginDataSource
}
