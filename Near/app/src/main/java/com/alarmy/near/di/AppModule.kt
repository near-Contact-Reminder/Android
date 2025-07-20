package com.alarmy.near.di

// @Module
// @InstallIn(SingletonComponent::class)
// object AppModule {
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
// }
