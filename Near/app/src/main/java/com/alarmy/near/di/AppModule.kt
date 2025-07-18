package com.alarmy.near.di

import android.content.Context
import androidx.room.Room
import com.alarmy.near.repository.ExampleRepository
import com.alarmy.near.repository.ExampleRepositoryImpl
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

//@Module
//@InstallIn(SingletonComponent::class)
//object AppModule {
//    @Provides
//    @Singleton
//    fun provideGson(): Gson = GsonBuilder().create()
//
//    @Provides
//    @Singleton
//    fun provideRetrofit(gson: Gson): Retrofit =
//        Retrofit.Builder()
//            .baseUrl("https://api.example.com/") // TODO: 실제 baseUrl로 변경
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//
//    @Provides
//    @Singleton
//    fun provideDatabase(app: Context): AppDatabase =
//        Room.databaseBuilder(app, AppDatabase::class.java, "app_db").build()
//
//    @Provides
//    @Singleton
//    fun provideGlideRequestManager(app: Context): RequestManager =
//        Glide.with(app)
//
//    @Provides
//    @Singleton
//    fun provideExampleRepository(): ExampleRepository = ExampleRepositoryImpl()
//}